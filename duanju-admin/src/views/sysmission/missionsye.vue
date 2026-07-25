<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="任务管理" name="first">
			<div style="display: inline-block;float: right;">
				<el-button style='margin:0 0 20px 20px;' :disabled="!isAuth('missionsye:add')" size="mini"
					type="primary" icon="document" @click="addTasks()">发布任务</el-button>
				<el-button style='margin:0 0 20px 20px;'
					:disabled="!isAuth('missionsye:delete') || checkBoxData.length <= 0" size="mini" type="danger"
					icon="document" @click="delectTasks()">批量删除</el-button>
			</div>
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="tableData">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column prop="classifyName" label="分类">
				</el-table-column>
				<el-table-column prop="titlePicture" label="任务图片">
					<template slot-scope="scope">
						<img :src="scope.row.titlePicture" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="任务标题" width="150">
				</el-table-column>
				<el-table-column prop="content" label="任务内容" v-html="content" width="300">
				</el-table-column>
				<el-table-column prop="luckyValue" label="幸运值">
				</el-table-column>
				<el-table-column prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row.id)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('missionsye:update')"
							@click="redactTask(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('missionsye:delete')"
							@click="delectTask(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="totalnum">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="分类管理" name="third">
			<div style="display: inline-block;float: right;">
				<el-button style='margin:0 0 20px 20px;' :disabled="!isAuth('missionsye:add')" size="mini"
					type="primary" icon="document" @click="classifyStair()">添加分类</el-button>
				<el-button style='margin:0 0 20px 20px;'
					:disabled="!isAuth('missionsye:delete') || checkBoxData.length <= 0" size="mini" type="danger"
					icon="document" @click="deleteStairs()">批量删除</el-button>
			</div>
			<el-table v-loading="tableDataLoading" @selection-change="changeFun" :data="classifyData">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column fixed type="index" label="编号" align="center" width="80">
					<template slot-scope="scope">
						<span>{{(page - 1) * limit + scope.$index + 1}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="classifyName" label="分类名称">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('missionsye:update')"
							@click="compile(scope.$index, scope.row)">编辑
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('missionsye:delete')"
							@click="deleteStair(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<!-- 添加分类 -->
			<el-dialog title="添加分类" :visible.sync="dialogFormVisible" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
					<el-input style="width:50%;" v-model="classifyName" type="text" placeholder="请输入分类名称"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 编辑分类 -->
			<el-dialog title="编辑分类" :visible.sync="dialogFormVisible2" center>
				<el-form :model="form">
					<el-form-item label="分类名称：" :label-width="formLabelWidth">
						<el-input v-model="form.classifyName" style="width:65%;"></el-input>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="CompileNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
		<el-tab-pane label="任务审核" name="thirdly">
			<div style="display: inline-block;float: right;">
				<el-button style='margin:0 0 20px 20px;' v-if="isAuth('missionsye:pass')" size="mini" type="primary"
					icon="document" @click="passClick()" :disabled="checkBoxData.length <= 0">通过</el-button>
				<el-button style='margin:0 0 20px 20px;' v-if="isAuth('missionsye:refuse')" size="mini" type="primary"
					icon="document" @click="refuseClick()" :disabled="checkBoxData.length <= 0">拒绝</el-button>
			</div>
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="auditData">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column fixed prop="titlePicture" label="任务图片" width="80">
					<template slot-scope="scope">
						<img :src="scope.row.titlePicture" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="任务标题" width="160">
				</el-table-column>
				<el-table-column prop="Tcontent" label="用户内容">
				</el-table-column>
				<el-table-column label="用户编号">
					<template slot-scope="scope">
						<span style="color: #4f9dec;cursor: pointer;"
							@click="updates(scope.row)">{{scope.row.userId}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="任务内容" width="300">
				</el-table-column>
				<el-table-column prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<span style="color: #4f9dec;" v-if="scope.row.state === 0 ">待提交</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 1 ">待审核</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 2 ">已通过</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 3 ">已拒绝</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 4 ">已放弃</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="lookTask(scope.row)">查看
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="totalnum">
				</el-pagination>
			</div>
			<!-- 查看任务-->
			<el-dialog title="查看任务" :visible.sync="dialogFormVisible3">
				<div style="text-align: center;">{{Tcontent?Tcontent : '暂无内容'}}</div>
				<div v-for="(item,index) in (picture|| '').split(',')" style="text-align: center;">
					<img :src="item" alt="" style="width:50%;height: 100%;">
				</div>
				<div slot="footer" class="dialog-footer">
					<!-- <el-button  @click="dialogFormVisible3 = false">取消</el-button> -->
					<el-button type="primary" :disabled="!isAuth('missionsye:pass')" @click="passClickrow(id)">通过
					</el-button>
				</div>
			</el-dialog>
			<!-- 任务拒绝弹框 -->
			<el-dialog title="判决理由" :visible.sync="dialogFormVisible5" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">拒绝理由：</span>
					<el-input style="width:50%;" v-model="auditContent" type="text" placeholder="请输入拒绝理由"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible5 = false">取 消</el-button>
					<el-button type="primary" @click="refuseto()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
		<el-tab-pane label="任务进行中" name="sixth">
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="proceedData">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column fixed prop="titlePicture" label="任务图片" width="80">
					<template slot-scope="scope">
						<img :src="scope.row.titlePicture" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="任务标题" width="160">
				</el-table-column>
				<el-table-column prop="Tcontent" label="用户内容">
				</el-table-column>
				<el-table-column label="用户编号">
					<template slot-scope="scope">
						<span style="color: #4f9dec;cursor: pointer;"
							@click="updates(scope.row)">{{scope.row.userId}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="任务内容" width="300">
				</el-table-column>
				<el-table-column prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<span style="color: #4f9dec;" v-if="scope.row.state === 0 ">待提交</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 1 ">待审核</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 2 ">已通过</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 3 ">已拒绝</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 4 ">已放弃</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('missionsye:warn')"
							@click="remind(scope.row)">提醒
						</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('missionsye:waive')"
							@click="abandon(scope.row)">放弃
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="totalnum">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="红包榜分类" name="fourth">
			<div style="display: inline-block;float: right;">
				<el-button style='margin:0 0 20px 20px;' :disabled="!isAuth('missionsye:add')" size="mini"
					type="primary" icon="document" @click="addClick()">添加队列</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="queueData">
				<el-table-column prop="name" label="队列名称" width="100">
				</el-table-column>
				<el-table-column prop="luckyValue" label="幸运值额度" width="120">
				</el-table-column>
				<el-table-column prop="sort" label="队列等级" width="80">
				</el-table-column>
				<el-table-column prop="recommendMoney" label="推荐奖" width="100">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.recommendMoney.toFixed(2)}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="handOut" label="派送红包" width="100">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.handOut.toFixed(2)}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="numberOfPeople" label="所需人数" width="100">
				</el-table-column>
				<el-table-column prop="startPerson" label="队列开始所需人数" width="150">
				</el-table-column>
				<el-table-column prop="intervalPerson" label="队列间隔人数" width="120">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('missionsye:update')"
							@click="compileQueue(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('missionsye:delete')"
							@click="deleteQueue(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<!-- 添加队列类型 -->
			<el-dialog title="添加队列类型" :visible.sync="dialogFormVisible6" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">队列名称：</span>
					<el-input style="width:50%;" v-model="name" type="text" placeholder="请输入队列名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">幸运值额度：</span>
					<el-input style="width:50%;" v-model="luckyValue" type="number" placeholder="请输入幸运值额度"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">队列等级：</span>
					<el-input style="width:50%;" v-model="sort" type="number" placeholder="请输入队列等级"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">推荐奖：</span>
					<el-input style="width:50%;" v-model="recommendMoney" type="number" placeholder="请输入推荐奖"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">派送红包：</span>
					<el-input style="width:50%;" v-model="handOut" type="number" placeholder="请输入派送红包"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">所需人数：</span>
					<el-input style="width:50%;" v-model="numberOfPeople" type="number" placeholder="请输入所需人数">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">队列开始所需人数：</span>
					<el-input style="width:50%;" v-model="startPerson" type="number" placeholder="请输入队列开始所需人数">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">队列间隔人数：</span>
					<el-input style="width:50%;" v-model="intervalPerson" type="number" placeholder="请输入队列间隔人数">
					</el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible6 = false">取 消</el-button>
					<el-button type="primary" @click="queueNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 编辑队列类型 -->
			<el-dialog title="编辑队列类型" :visible.sync="dialogFormVisible7" center>
				<el-form :model="form2">
					<el-form-item label="队列名称：" :label-width="formLabelWidth">
						<el-input v-model="form2.name" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="幸运值额度：" :label-width="formLabelWidth">
						<el-input v-model="form2.luckyValue" style="width:65%;" type="number"></el-input>
					</el-form-item>
					<el-form-item label="队列等级：" :label-width="formLabelWidth">
						<el-input v-model="form2.sort" readonly style="width:65%;" type="number"></el-input>
					</el-form-item>
					<el-form-item label="推荐奖：" :label-width="formLabelWidth">
						<el-input v-model="form2.recommendMoney" style="width:65%;" type="number"></el-input>
					</el-form-item>
					<el-form-item label="派送红包：" :label-width="formLabelWidth">
						<el-input v-model="form2.handOut" style="width:65%;" type="number"></el-input>
					</el-form-item>
					<el-form-item label="所需人数：" :label-width="formLabelWidth">
						<el-input v-model="form2.numberOfPeople" style="width:65%;" type="number"></el-input>
					</el-form-item>
					<el-form-item label="队列开始所需人数：" :label-width="formLabelWidth">
						<el-input v-model="form2.startPerson" style="width:65%;" type="number"></el-input>
					</el-form-item>
					<el-form-item label="队列间隔人数：" :label-width="formLabelWidth">
						<el-input v-model="form2.intervalPerson" style="width:65%;" type="number"></el-input>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible7 = false">取 消</el-button>
					<el-button type="primary" @click="CompileQueueTo()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
		<el-tab-pane label="红包榜审核" name="fifth">
			<div style="display: inline-block;float: right;">
				<el-button style='margin:0 0 20px 20px;' v-if="isAuth('missionsye:pass')" size="mini" type="primary"
					icon="document" @click="queueClick()" :disabled="checkBoxData.length <= 0">通过</el-button>
				<el-button style='margin:0 0 20px 20px;' v-if="isAuth('missionsye:refuse')" size="mini" type="primary"
					icon="document" @click="queueRefuse()" :disabled="checkBoxData.length <= 0">拒绝</el-button>
			</div>
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="checkData">
				<el-table-column type="selection">
				</el-table-column>
				<el-table-column prop="name" label="队列名称">
				</el-table-column>
				<el-table-column prop="imageUrl" label="用户图像">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="auditContent" label="审核内容">
				</el-table-column>
				<el-table-column prop="nickName" label="用户名称">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span style="color: #4f9dec;" v-if="scope.row.state === 0 ">待审核</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 1 ">通过</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 2 ">拒绝</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="totalnum">
				</el-pagination>
			</div>
			<!-- 队列拒绝弹框 -->
			<el-dialog title="判决理由" :visible.sync="dialogFormVisible8" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">拒绝理由：</span>
					<el-input style="width:50%;" v-model="auditContent" type="text" placeholder="请输入拒绝理由"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible8 = false">取 消</el-button>
					<el-button type="primary" @click="queueRefuseto()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
	</el-tabs>
</template>

<script>
	export default {
		data() {
			return {
				limit: 5,
				page: 1,
				state: 0,
				sort: 0,
				id: '',
				totalnum: 0,
				readonly: true,
				name: '',
				content: '',
				imageUrl: '',
				luckyValue: '',
				recommendMoney: '',
				handOut: '',
				numberOfPeople: '',
				startPerson: '',
				intervalPerson: '',
				proceedData: [],
				queueData: [],
				form2: {
					id: '',
					name: '',
					luckyValue: '',
					sort: '',
					recommendMoney: '',
					handOut: '',
					numberOfPeople: '',
					startPerson: '',
					intervalPerson: ''

				},
				openValue: 1,
				closeValue: 2,
				helpTakeId: '',
				helpMaintainId: '',
				auditContent: '同意',
				classifyId: '',
				classifyName: '',
				Tcontent: '',
				picture: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				tableData: [],
				info: {
					stockDate: this.getNowTime(), //日期
				},
				form: {
					id: '',
					sort: '',
					state: '',
					classifyName: '',
				},
				auditData: [],
				checkData: [],
				classifyData: [],
				checkBoxData: [], //多选框选择的值
			}
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			// 状态
			change(state, id) {
				this.$http({
					url: this.$http.adornUrl(
						`helpTaskPlatform/updateHelpTaskPlatformStateById?id=${id}&state=${state}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				})
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.limit = val;
				this.classifySelect()
			},
			handleCurrentChange2(val) {
				this.page = val;
				this.classifySelect()
			},
			handleSizeChange1(val) {
				this.limit = val;
				this.auditSelect()
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.auditSelect()
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '任务管理') {
					this.page = 1
					this.limit = 5
					this.dataSelect()
				}
				if (tab._props.label == '分类管理') {
					this.page = 1
					this.limit = 5
					this.classifySelect()
				}
				if (tab._props.label == '任务审核') {
					this.page = 1
					this.limit = 5
					this.auditSelect()
				}
				if (tab._props.label == '任务进行中') {
					this.page = 1
					this.limit = 5
					this.proceedSelect()
				}
				if (tab._props.label == '红包榜分类') {
					this.page = 1
					this.limit = 5
					this.queueSelect()
				}
				if (tab._props.label == '红包榜审核') {
					this.page = 1
					this.limit = 5
					this.checkSelect()
				}
			},
			// 修改任务
			redactTask(row) {
				this.$router.push({
					name: 'missionRedact',
					query: {
						id: row.id
					}
				});
			},
			//添加任务
			addTasks() {
				this.$router.push({
					path: '/missionAdd'
				});
			},
			//任务通过
			passClick(id, state) {
				this.state = 1
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$http({
					url: this.$http.adornUrl(
						`helpTaskPlatform/auditHelpGetTask/${ids}/${this.state}/${this.auditContent}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.auditSelect()
						}
					})
				})
			},
			//查看任务通过
			passClickrow(id) {
				this.state = 1
				this.$http({
					url: this.$http.adornUrl(
						`helpTaskPlatform/auditHelpGetTask/${id}/${this.state}/${this.auditContent}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible3 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.auditSelect()
						}
					})
				})
			},
			//任务拒绝
			refuseClick(id) {
				this.state = 2
				this.auditContent = ''
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				if (ids.length > 1) {
					this.$message({
						message: '拒绝不能批量，需要单条拒绝',
						type: 'error',
						duration: 1500,
					})
				} else {
					this.dialogFormVisible5 = true
				}
			},
			refuseto(id) {
				if (this.auditContent == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入拒绝理由',
						type: 'warning'
					});
					return
				}
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$http({
					url: this.$http.adornUrl(
						`helpTaskPlatform/auditHelpGetTask/${ids}/${this.state}/${this.auditContent}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible5 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.auditContent = ''
							this.auditSelect()
						}
					})
				})
			},
			// 查看任务
			lookTask(row) {
				this.dialogFormVisible3 = true
				this.Tcontent = row.Tcontent
				this.picture = row.picture
				this.id = row.id
			},
			// 批量删除任务
			delectTasks(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(
							`helpTaskPlatform/deleteHelpTaskPlatformByIds/?ids=${ids}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '批量删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			//删除任务
			delectTask(row) {
				let id = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`helpTaskPlatform/deleteHelpTaskPlatformById?id=${id}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = '';
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 添加分类弹框
			classifyStair() {
				this.dialogFormVisible = true
			},
			// 添加分类确定
			StairNoticeTo() {
				if (this.classifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('platformClassify/saveHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'classifyName': this.classifyName,
						'state': this.state,
						'sort': this.sort,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '分类添加成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.classifyName = ''
							this.classifySelect()
						}
					})
				})
			},
			// 修改分类
			compile(index, rows) {
				this.dialogFormVisible2 = true;
				this.form.id = rows.id;
				this.form.state = rows.state;
				this.form.classifyName = rows.classifyName;
				this.form.sort = rows.sort;
			},
			// 修改分类确定
			CompileNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('platformClassify/updateHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'sort': this.form.sort,
						'state': this.form.state,
						'classifyName': this.form.classifyName,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible2 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.classifySelect()
						}
					})
				})
			},
			// 批量删除分类
			deleteStairs(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`platformClassify/deleteClassifyByIds?ids=${ids}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '批量删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.classifySelect()
							}
						})
					})
				}).catch(() => {})
			},
			//删除分类
			deleteStair(row) {
				let id = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`platformClassify/deleteClassifyById?id=${id}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = '';
								this.classifySelect()
							}
						})
					})
				}).catch(() => {})
			},
			//添加队列类型
			addClick() {
				this.dialogFormVisible6 = true
			},
			//添加队列类型
			queueNoticeTo() {
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入队列名称',
						type: 'warning'
					});
					return
				}
				if (this.luckyValue == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入幸运值额度',
						type: 'warning'
					});
					return
				}
				if (this.sort == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入队列等级',
						type: 'warning'
					});
					return
				}
				if (this.recommendMoney == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入推荐奖',
						type: 'warning'
					});
					return
				}
				if (this.handOut == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入派送红包',
						type: 'warning'
					});
					return
				}
				if (this.numberOfPeople == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入所需人数',
						type: 'warning'
					});
					return
				}
				if (this.startPerson == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入队列开始所需人数',
						type: 'warning'
					});
					return
				}
				if (this.intervalPerson == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入间隔人数',
						type: 'warning'
					});
					return
				}
				let state = 1
				this.$http({
					url: this.$http.adornUrl('lucky/insertLuckyQueue'),
					method: 'post',
					data: this.$http.adornData({
						'name': this.name,
						'luckyValue': this.luckyValue,
						'sort': this.sort,
						'recommendMoney': this.recommendMoney,
						'handOut': this.handOut,
						'numberOfPeople': this.numberOfPeople,
						'startPerson': this.startPerson,
						'intervalPerson': this.intervalPerson,
						'state': state,
						'createTime': this.info.stockDate
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible6 = false
					if (data.status == 0) {
						this.$message({
							message: '队列类型添加成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = ''
								this.luckyValue = ''
								this.sort = ''
								this.recommendMoney = ''
								this.handOut = ''
								this.numberOfPeople = ''
								this.startPerson = ''
								this.intervalPerson = ''
								this.queueSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
						})
					}
				})
			},
			// 编辑队列类型
			compileQueue(index, rows) {
				let state = this.$cookie.get('state')
				if (state !== '1') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '无权操作!',
						type: 'warning'
					});
				} else {
					this.dialogFormVisible7 = true;
					this.form2.id = rows.id;
					this.form2.name = rows.name;
					this.form2.luckyValue = rows.luckyValue;
					this.form2.sort = rows.sort;
					this.form2.recommendMoney = rows.recommendMoney;
					this.form2.handOut = rows.handOut;
					this.form2.numberOfPeople = rows.numberOfPeople;
					this.form2.startPerson = rows.startPerson;
					this.form2.intervalPerson = rows.intervalPerson
				}

			},
			// 编辑队列类型
			CompileQueueTo() {
				let state = 1
				this.$http({
					url: this.$http.adornUrl('lucky/updateLuckyQueue'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form2.id,
						'state': state,
						'name': this.form2.name,
						'luckyValue': this.form2.luckyValue,
						'sort': this.form2.sort,
						'recommendMoney': this.form2.recommendMoney,
						'handOut': this.form2.handOut,
						'numberOfPeople': this.form2.numberOfPeople,
						'createTime': this.info.stockDate,
						'startPerson': this.form2.startPerson,
						'intervalPerson': this.form2.intervalPerson
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible7 = false
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.queueSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
						})
					}
				})
			},
			//删除队列类型
			deleteQueue(row) {
				let state = this.$cookie.get('state')
				let id = row.id
				if (state !== '1') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '无权操作!',
						type: 'warning'
					});
				} else {
					this.$confirm(`确定删除此条信息?`, '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$http({
							url: this.$http.adornUrl(`lucky/deleteLuckyQueue/?id=${id}`),
							method: 'post',
							data: this.$http.adornData({})
						}).then(({
							data
						}) => {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.queueSelect()
								}
							})
						})
					}).catch(() => {})
				}
			},
			//队列审核通过
			queueClick(id, state) {
				var states = state ? [state] : this.checkBoxData.map(item => {
					return item.state
				})
				for (var i in states) {
					if (states[i] == 0) {
						this.state = 1
						var ids = id ? [id] : this.checkBoxData.map(item => {
							return item.id
						})
						this.$http({
							url: this.$http.adornUrl(
								`lucky/auditLuckyQueueRecord?ids=${ids}&state=${this.state}&auditContent=${this.auditContent}`
								),
							method: 'post',
							data: this.$http.adornData({})
						}).then(({
							data
						}) => {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.checkSelect()
								}
							})
						})
					} else {
						this.$message({
							message: '队列已经审核，请勿重复审核！',
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.checkSelect()
							}
						})
					}
				}
			},
			//队列拒绝
			queueRefuse(id) {
				this.state = 2
				this.auditContent = ''
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				if (ids.length > 1) {
					this.$message({
						message: '拒绝不能批量，需要单条拒绝',
						type: 'error',
						duration: 1500,
					})
				} else {
					this.dialogFormVisible8 = true
				}
			},
			queueRefuseto(id) {
				if (this.auditContent == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入拒绝理由',
						type: 'warning'
					});
					return
				}
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$http({
					url: this.$http.adornUrl(
						`lucky/auditLuckyQueueRecord?ids=${ids}&state=${this.state}&auditContent=${this.auditContent}`
						),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible8 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.auditContent = ''
							this.checkSelect()
						}
					})
				})
			},
			// 获取任务数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpTaskPlatform/selectHelpTaskPlatformList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData.list
					this.totalnum = returnData.totalCount
				})
			},
			// 获取分类数据
			classifySelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('platformClassify/selectClassifyList'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.classifyData = returnData
				})
			},
			// 获取任务审核数据
			auditSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpTaskPlatform/selectHelpGetTaskList'),
					method: 'get',
					params: this.$http.adornParams({
						'limit': this.limit,
						'page': this.page
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.auditData = returnData.list
					this.totalnum = returnData.totalCount
				})
			},
			// 获取任务进行中数据
			proceedSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpTaskPlatform/selectUnderwayHelpGetTaskList'),
					method: 'get',
					params: this.$http.adornParams({
						'limit': this.limit,
						'page': this.page
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.proceedData = returnData.list
					this.totalnum = returnData.totalCount
				})
			},
			// 提醒任务
			remind(row) {
				this.$http({
					url: this.$http.adornUrl(`helpTaskPlatform/helpGetTaskRemind/${row.id}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '提醒成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.proceedSelect()
						}
					})
				})
			},
			// 放弃任务
			abandon(row) {
				this.$confirm(`确定放弃此条任务?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(
							`helpTaskPlatform/selectUnderwayHelpGetTaskList/${row.id}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.proceedSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取队列分类数据
			queueSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('lucky/selectLuckyQueueList'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.queueData = returnData
				})
			},
			// 获取红包榜审核数据
			checkSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('lucky/selectLuckyQueueRecordList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.checkData = returnData.list
					this.totalnum = returnData.totalCount
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style>

</style>
