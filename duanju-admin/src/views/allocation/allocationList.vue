<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="全局系统配置" name="first">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="服务费配置" name="second">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
						  <div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<!-- <el-tab-pane label="首页配置" name="thirdly">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="是否启用" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amendWhether(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
					<el-form :model="form2">
						<el-form-item label="配置类型：" :label-width="formLabelWidth">
							<el-input v-model="form2.min" style="width:65%;" readonly></el-input>
						</el-form-item>
						<el-form-item label="是否启用：" :label-width="formLabelWidth">
							<el-select v-model="form2.value" placeholder="请选择类型" style="width:65%;">
								<el-option v-for="item in values" :key="item.value" :label="item.label"
									:value="item.label">
								</el-option>
							</el-select>
						</el-form-item>
					</el-form>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible1 = false">取 消</el-button>
						<el-button type="primary" @click="WhetherNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
			</el-tab-pane> -->
			<el-tab-pane label="微信配置" name="fifth">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="支付宝配置" name="sixth">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 4;overflow: hidden;">
								{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="短信配置" name="seventh">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="客服配置" name="kefu">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="文件上传配置" name="wenjian">
			  <el-table v-loading="tableDataLoading" :data="tableData">
			    <el-table-column fixed prop="type" label="编号" align="center" width="80">
			    </el-table-column>
			    <el-table-column prop="min" label="类型">
			    </el-table-column>
			    <el-table-column prop="value" label="内容" width="500">
			      <template slot-scope="scope">
			        <div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">{{scope.row.value}}</div>
			      </template>
			    </el-table-column>
			    <el-table-column prop="createAt" label="创建时间">
			    </el-table-column>
			    <el-table-column label="操作" prop="id" width="120">
			      <template slot-scope="scope">
			        <el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
			          @click="amend(scope.$index, scope.row)">编辑
			        </el-button>
			      </template>
			    </el-table-column>
			  </el-table>
			</el-tab-pane>
			<el-tab-pane label="协议配置" name="xieyi">
			  <el-table v-loading="tableDataLoading" :data="tableData">
			    <el-table-column fixed prop="type" label="编号" align="center" width="80">
			    </el-table-column>
			    <el-table-column prop="min" label="类型">
			    </el-table-column>
			    <el-table-column prop="value" label="内容" width="500">
			      <template slot-scope="scope">
			        <div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">{{scope.row.value}}</div>
			      </template>
			    </el-table-column>
			    <el-table-column prop="createAt" label="创建时间">
			    </el-table-column>
			    <el-table-column label="操作" prop="id" width="120">
			      <template slot-scope="scope">
			        <el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
			          @click="amend(scope.$index, scope.row)">编辑
			        </el-button>
			      </template>
			    </el-table-column>
			  </el-table>
			</el-tab-pane>
			<el-tab-pane label="开关配置" name="kaiguan">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型" width="250">
					</el-table-column>
					<el-table-column prop="value" label="内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="图片配置" name="image">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型" width="250">
					</el-table-column>
					<el-table-column prop="value" label="内容">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="签到积分设置" name="task_sign_in">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="天数" width="250">
					</el-table-column>
					<el-table-column prop="value" label="积分数">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="每日任务配置" name="task_day">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="任务名称" width="250">
					</el-table-column>
					<el-table-column prop="value" label="任务积分">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="充值配置" name="charge">
				<div style = "float: right;margin-right:2%;">
					<el-button style = "margin: 10px 0;" size = "mini" type = "primary" icon = "document"
							   @click = "chargeAddFormVisible = true;form.conditionFrom = 'charge'">添加充值配置页</el-button>
				</div>

				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="id" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="value" label="充值金额">
					</el-table-column>
					<el-table-column prop="max" label="所得积分">
					</el-table-column>
					<el-table-column prop="min" label="赠送积分">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="250">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
							<el-button size="mini" type = "danger" :disabled="!isAuth('allocationList:update')"
								@click="remove(scope.$index, scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
		</el-tabs>
		<el-dialog title="添加" :visible.sync="chargeAddFormVisible" center>
			<el-form :model="form">
				<el-form-item label="充值金额：" :label-width="formLabelWidth">
					<el-input v-model="form.value" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="所得积分：" :label-width="formLabelWidth">
					<el-input v-model="form.max" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="赠送积分：" :label-width="formLabelWidth">
					<el-input v-model="form.min" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="chargeAddFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addCommonInfo">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 修改弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<div v-if="form.conditionFrom=='charge'">
					<!-- 充值配置 -->
					<el-form-item label="充值金额：" :label-width="formLabelWidth">
						<el-input v-model="form.value" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="所得积分：" :label-width="formLabelWidth">
						<el-input v-model="form.max" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="赠送积分：" :label-width="formLabelWidth">
						<el-input v-model="form.min" style="width:65%;"></el-input>
					</el-form-item>
				</div>
				<div v-else>
					<el-form-item label="配置类型：" :label-width="formLabelWidth">
						<el-input v-model="form.min" style="width:65%;" readonly></el-input>
					</el-form-item>
					<el-form-item label="内容：" :label-width="formLabelWidth">
						<quill-editor v-if="form.type==144 || form.type==146 || form.type==153 || form.type==154 || form.type==155 " ref="myTextEditor" v-model="form.value" :options="quillOption"
						  style="padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
						</quill-editor>
						<div v-else-if="form.conditionFrom=='image'">
							<!-- <el-input v-model="form.value" style="width:65%;"></el-input> -->
							<el-upload class="avatar-uploader" v-model="form.value"
								:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
								:on-success="handleAvatarSuccess1">
								<img v-if="form.value" :src="form.value" class="avatar" style="width: 148px;height: 148px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
							</el-upload>
						</div>
						<div v-else-if="form.conditionFrom=='kaiguan'">
							<div>
								<el-radio-group v-model="form.value">
									<el-radio label="是">是</el-radio>
									<el-radio label="否">否</el-radio>
								</el-radio-group>
							</div>
						</div>
						<el-input v-else v-model="form.value"  :rows="4" style="width:65%;"></el-input>
						<!-- <el-input v-model="form.value" style="width:65%;"></el-input> -->
					</el-form-item>
				</div>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../locality/quill-config.js'
	export default {
		components: {
			quillEditor
		},
		data() {
			return {
				chargeAddFormVisible: false,
				quillOption: quillConfig,
				openValue: '是',
				closeValue: '否',
				limit: 10,
				page: 0,
				min: '',
				value: '',
				id: '',
				condition: 'xitong',
				activeName: 'first',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableDataLoading: true,
				formLabelWidth: '200px',
				form: {
					id: '',
					min: '',
					max: '',
					value: '',
					type: '',
					conditionFrom: '',
				},
				form2: {
					id: '',
					min: '',
					value: '',
					type: '',
				},
				values: [{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					}
				],
				tableData: [],
				checkBoxData: [] //多选框选择的值
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect();
			},
			handleCurrentChange(val) {
				this.page = val - 1;
				this.dataSelect();
			},
			handleClick(tab, event) {
				if (tab._props.label == '全局系统配置') {
					this.condition = 'xitong'
					this.dataSelect()
				}
				if (tab._props.label == '首页配置') {
					this.condition = 'shouye'
					this.dataSelect()
				}
				if (tab._props.label == '微信配置') {
					this.condition = 'weixin'
					this.dataSelect()
				}
				if (tab._props.label == '支付宝配置') {
					this.condition = 'zhifubao'
					this.dataSelect()
				}
				if (tab._props.label == '短信配置') {
					this.condition = 'duanxin'
					this.dataSelect()
				}
				if (tab._props.label == '任务系统配置') {
					this.condition = 'renwu'
					this.dataSelect()
				}
				if (tab._props.label == '文件上传配置') {
				  this.condition = 'oss'
				  this.dataSelect()
				}
				if (tab._props.label == '服务费配置') {
				  this.condition = 'fuwufei'
				  this.dataSelect()
				}
				if (tab._props.label == '协议配置') {
				  this.condition = 'xieyi'
				  this.dataSelect()
				}
				if (tab._props.label == '图片配置') {
					this.condition = 'image'
					this.dataSelect()
				}
				if (tab._props.label == '开关配置') {
					this.condition = 'kaiguan'
					this.dataSelect()
				}
				if (tab._props.label == '客服配置') {
					this.condition = 'kefu'
					this.dataSelect()
				}
				if (tab._props.label == '签到积分设置') {
					this.condition = 'task_sign_in'
					this.dataSelect()
				}
				if (tab._props.label == '每日任务配置') {
					this.condition = 'task_day'
					this.dataSelect()
				}
				if (tab._props.label == '充值配置') {
					this.condition = 'charge'
					this.dataSelect()
				}

			},
			// 修改弹框
			amend(index, rows) {
				this.dialogFormVisible = true;
				this.form.id = rows.id;
				this.form.type = rows.type
				this.form.min = rows.min;
				this.form.value = rows.value;
				this.form.max = rows.max;
				this.form.createAt = rows.createAt;
				this.form.conditionFrom = rows.conditionFrom
			},
			// 删除
			remove(index, rows) {
				let delid = rows.id
				this.$confirm(`确定删除此条配置?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`common/${delid}`),
						method: 'delete',
						data: this.$http.adornData({})
					}).then(({data}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {
				})
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('common/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'type': this.form.type,
						'value': this.form.value,
						'min': this.form.min,
						'max': this.form.max,
						'createAt': this.form.createAt,
						'conditionFrom': this.form.conditionFrom
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 修改
			addCommonInfo() {
				this.$http({
					url: this.$http.adornUrl('common'),
					method: 'post',
					data: this.$http.adornData({
						'value': this.form.value,
						'min': this.form.min,
						'max': this.form.max,
						'conditionFrom': this.form.conditionFrom
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.chargeAddFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 修改首页
			amendWhether(index, rows) {
				this.dialogFormVisible1 = true;
				this.form2.id = rows.id;
				this.form2.type = rows.type
				this.form2.min = rows.min;
				this.form2.value = rows.value;
				this.form2.max = rows.max;
				this.form2.createAt = rows.createAt;
				this.form2.conditionFrom = rows.conditionFrom
			},
			WhetherNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('common/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form2.id,
						'type': this.form2.type,
						'value': this.form2.value,
						'min': this.form2.min,
						'max': this.form2.max,
						'createAt': this.form2.createAt,
						'conditionFrom': this.form2.conditionFrom
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dialogFormVisible1 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`common/type/condition/${this.condition}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData
					}
				})
			},
			handleAvatarSuccess1(file, fileList) {
				this.form.value = file.data
				console.log('file.data',file.data)
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped="scoped">
	.eit {
		height: 120px;
	}
</style>
