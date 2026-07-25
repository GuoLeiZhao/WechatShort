<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="首页公告" name="first">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('fitmentList:add')" size="mini" type="primary"
					icon="document" @click="addNotice">添加公告</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="title" label="标题">
				</el-table-column>
				<el-table-column prop="type" label="类型">
					<template slot-scope="scope">
						<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.type === 'url' ">链接</span>
						<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.type === 'word' ">文本</span>
					</template>
				</el-table-column>
				<el-table-column prop="url" label="链接">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('fitmentList:update')"
							@click="updates(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('fitmentList:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
			<!-- 添加弹框 -->
			<el-dialog title="添加公告" :visible.sync="dialogFormVisible" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
					<el-input style="width:50%;" v-model="title" placeholder="请输入公告标题"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
					<el-input style="width: 50%;" v-model="url" placeholder="请输入公告链接"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
					<el-select v-model="type" placeholder="请选择公告类型" style="width:50%;">
						<el-option v-for="item in types" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="releasgongaoTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改弹框 -->
			<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
				<el-form :model="form">
					<el-form-item label="标题：" :label-width="formLabelWidth">
						<el-input v-model="form.title" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="链接：" :label-width="formLabelWidth">
						<el-input v-model="form.url" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="类型：" :label-width="formLabelWidth">
						<el-select v-model="form.type" placeholder="请选择类型" style="width:65%;">
							<el-option v-for="item in types" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
		<el-tab-pane label="首页导航" name="second">
			<el-table v-loading="tableDataLoading" :data="homeData">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="title" label="名称">
				</el-table-column>
				<el-table-column prop="url" label="路由">
				</el-table-column>
				<el-table-column prop="content" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('fitmentList:update')"
							@click="compile(scope.$index, scope.row)">编辑
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="homeData.length">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="首页推广" name="third">
			<el-table v-loading="tableDataLoading" :data="homeData">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="title" label="名称">
				</el-table-column>
				<el-table-column prop="url" label="路由">
				</el-table-column>
				<el-table-column prop="content" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('fitmentList:update')"
							@click="compile(scope.$index, scope.row)">编辑
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="homeData.length">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="升级配置" name="fourth">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('fitmentList:add')" size="mini" type="primary"
					icon="document" @click="addUpgrade">添加升级</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="upgradeData">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="wgtUrl" label="统一地址">
				</el-table-column>
				<el-table-column prop="androidWgtUrl" label="安卓">
				</el-table-column>
				<el-table-column prop="iosWgtUrl" label="苹果">
				</el-table-column>
				<el-table-column prop="version" label="版本">
				</el-table-column>
				<el-table-column prop="content" label="升级方式">
					<template slot-scope="scope">
						<span>{{scope.row.method == 'true' ? '强制升级' : '普通升级'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('fitmentList:update')"
							@click="upgradebj(scope.$index, scope.row)">编辑
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('fitmentList:delete')"
							@click="upgradelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="upgradeData.length">
				</el-pagination>
			</div>
			<!-- 添加升级弹框 -->
			<el-dialog title="添加升级" :visible.sync="dialogFormVisible3" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>统一地址：</span>
					<el-input style="width: 50%;" v-model="wgtUrl" placeholder="请输入统一地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>安卓地址：</span>
					<el-input style="width:50%;" v-model="androidWgtUrl" placeholder="请输入安卓地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>苹果地址：</span>
					<el-input style="width: 50%;" v-model="iosWgtUrl" placeholder="请输入苹果地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>版本号：</span>
					<el-input style="width: 50%;" v-model="version" placeholder="请输入版本号"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>升级方式：</span>
					<el-select v-model="method" placeholder="请选择升级方式" style="width:50%;">
						<el-option v-for="item in methods" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>升级内容：</span>
					<el-input style="width: 50%;" v-model="des" placeholder="请输入升级内容"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="upgradaddTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改升级弹框 -->
			<el-dialog title="修改" :visible.sync="dialogFormVisible4" center>
				<el-form :model="formupgrad">
					<el-form-item label="统一地址：" :label-width="formLabelWidth">
						<el-input v-model="formupgrad.wgtUrl" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="安卓地址：" :label-width="formLabelWidth">
						<el-input v-model="formupgrad.androidWgtUrl" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="苹果地址：" :label-width="formLabelWidth">
						<el-input v-model="formupgrad.iosWgtUrl" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="版本号：" :label-width="formLabelWidth">
						<el-input v-model="formupgrad.version" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="升级方式：" :label-width="formLabelWidth">
						<el-select v-model="formupgrad.method" placeholder="请选择升级方式" style="width:65%;">
							<el-option v-for="item in methods" :key="item.value" :label="item.label"
								:value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="升级内容：" :label-width="formLabelWidth">
						<el-input v-model="formupgrad.des" style="width:65%;"></el-input>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible4 = false">取 消</el-button>
					<el-button type="primary" @click="upgradbjTo()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
		<el-tab-pane label="精选商品" name="fifth">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('fitmentList:add')" size="mini" type="primary"
					icon="document" @click="choiaddNotice()">添加商品</el-button>
				<el-button style='margin: 10px 0;'
					:disabled="!isAuth('fitmentList:delete') || checkBoxData.length <= 0 " size="mini" type="danger"
					icon="document" @click="choideletes()">批量删除</el-button>
			</div>
			<el-table v-loading="tableDataLoading" @selection-change="changeFun" :data="choicenData.list">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="itempic" label="商品图片">
					<template slot-scope="scope">
						　　<img :src="scope.row.itempic" width="40" height="40" />
					</template>
				</el-table-column>
				<el-table-column fixed prop="itemtitle" label="商品名称" width="200">
				</el-table-column>
				<el-table-column fixed prop="couponmoney" label="优惠券(元)">
				</el-table-column>
				<el-table-column fixed prop="itemendprice" label="券后价(元)">
				</el-table-column>
				<el-table-column fixed prop="itemsale" label="销量">
				</el-table-column>
				<el-table-column fixed prop="shopname" label="店铺名称">
				</el-table-column>
				<el-table-column label="操作" prop="id" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('fitmentList:delete')"
							@click="choidelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="choicenData.totalCount">
				</el-pagination>
			</div>
			<!-- 添加弹框 -->
			<el-dialog title="添加" customClass="customWidth" :visible.sync="dialogFormVisible5" center>
				<div style="position: relative;display: inline-block;float: right;">
					<el-input style="width: 200px;" @keydown.enter.native="shousuo" placeholder="搜索商品"
						v-model="keyword"></el-input>
					<span @click="shousuo" style="position: absolute;right: 0;top:8px;">
						<icon-svg name="shousuo" class="site-sidebar__menu-icon"></icon-svg>
					</span>
				</div>
				<el-table v-loading="tableDataLoading" height="450" :data="choicenData2">
					<el-table-column fixed type="index" label="编号" align="center" width="50">
						<template slot-scope="scope">
							<span>{{(page - 1) * limit + scope.$index + 1}}</span>
						</template>
					</el-table-column>
					<el-table-column fixed prop="itempic" label="商品图片">
						<template slot-scope="scope">
							　　<img :src="scope.row.itempic" width="40" height="40" style="border-radius: 50%;" />
						</template>
					</el-table-column>
					<el-table-column fixed prop="itemtitle" label="商品名称" width="200">
					</el-table-column>
					<el-table-column fixed prop="couponmoney" label="优惠券(元)">
					</el-table-column>
					<el-table-column fixed prop="itemendprice" label="券后价(元)">
					</el-table-column>
					<el-table-column fixed prop="itemsale" label="销量">
					</el-table-column>
					<el-table-column fixed prop="shopname" label="店铺名称">
					</el-table-column>
					<el-table-column label="操作" prop="id">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="releasNoticeTo(scope.row)">添加
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-dialog>
		</el-tab-pane>
		<el-tab-pane label="首页轮播" name="sixth">
			<div>
				<el-row>
					<el-col :span="6" class="cards" v-if="isAuth('fitmentList:add')">
						<div class="adver_main box">
							<a href="javascript:;" @click="addNoticelun">
								+ 新增轮播图
							</a>
						</div>
					</el-col>
					<el-col :span="6" class="cards" v-for="(item,index) in bannerData" :key="index">
						<div class="adver_main bannerManin">
							<img :src="item.imageUrl" alt="">
							<span>{{item.url}}</span>
						</div>
						<div class="bannerbtn">
							<a href="javascript:;" v-if="isAuth('fitmentList:update')" @click="compilelun(item)">编辑</a>
							<a href="javascript:;" v-if="isAuth('fitmentList:delete')"
								@click="deleteslun(item.id)">删除</a>
						</div>
					</el-col>
				</el-row>
				<!-- 添加弹框 -->
				<el-dialog title="添加轮播图信息" :visible.sync="dialogFormVisible6" center>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
						<el-input style="width:50%;" v-model="url" placeholder="请输入轮播图链接"></el-input>
					</div>
					<div style="margin-bottom: 10px;display: flex;">
						<span style="width: 200px;display: inline-block;text-align: right;">轮播图片：</span>
						<div
							style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
							<el-upload class="avatar-uploader" v-model="imageUrl"
								:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
								:on-success="handleAvatarSuccess">
								<img v-if="imageUrl" :src="imageUrl" class="avatar"
									style="border-radius: 6px;width: 148px;height: 148px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</div>
					</div>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible6 = false">取 消</el-button>
						<el-button type="primary" @click="releaslunNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
				<!-- 编辑弹框 -->
				<el-dialog title="修改" :visible.sync="dialogFormVisible7" center>
					<el-form :model="form1">
						<el-form-item label="链接：" :label-width="formLabelWidth">
							<el-input v-model="form1.url" style="width:65%;"></el-input>
						</el-form-item>
						<el-form-item label="轮播图片：" :label-width="formLabelWidth">
							<div
								style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
								<el-upload class="avatar-uploader" v-model="imageUrl2"
									:action="$http.adornUrl('alioss/upload')" 
									:show-file-list="false" :on-success="handleAvatarSuccess2">
									<img v-if="form1.imageUrl" :src="form1.imageUrl" class="avatar"
										style="border-radius: 6px;width: 148px;height: 148px;" />
									<i v-else class="el-icon-plus avatar-uploader-icon"></i>
								</el-upload>
							</div>
						</el-form-item>
					</el-form>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible7 = false">取 消</el-button>
						<el-button type="primary" @click="upgradbianTo()">确 定</el-button>
					</div>
				</el-dialog>
			</div>
		</el-tab-pane>
		<!-- 首页编辑 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible2" center>
			<el-form :model="formcomp">
				<el-form-item label="标题：" :label-width="formLabelWidth">
					<el-input v-model="formcomp.title" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="路由：" :label-width="formLabelWidth">
					<el-input v-model="formcomp.url" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="图片：" :label-width="formLabelWidth">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="imageUrl"
							:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
							:on-success="handleAvatarSuccess3">
							<img v-if="formcomp.imageUrl" :src="formcomp.imageUrl" class="avatar"
								style="border-radius: 6px;width: 86px;height: 86px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="compileNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</el-tabs>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				state: 1,
				url: '',
				title: '',
				type: '',
				id: '',
				nav: '',
				keyword: '',
				imageUrl: '',
				androidWgtUrl: '',
				iosWgtUrl: '',
				wgtUrl: '',
				version: '',
				des: '',
				checkBoxData: [], //多选框选择的值
				method: 'false',
				methods: [{
						value: 'false',
						label: '普通升级'
					},
					{
						value: 'true',
						label: '强制升级'
					}
				],
				formupgrad: {
					id: '',
					androidWgtUrl: '',
					iosWgtUrl: '',
					wgtUrl: '',
					version: '',
					des: '',
					method: ''
				},
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: true,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				tableData: [],
				homeData: [],
				choicenData2: [],
				upgradeData: [],
				choicenData: [],
				types: [{
						value: 'word',
						label: '文字'
					},
					{
						value: 'url',
						label: '链接'
					}
				],
				url: '',
				imageUrl: '',
				imageUrl2: '',
				id: '',
				bannerData: [],
				form1: {
					id: '',
					url: '',
					imageUrl: ''
				},
				form: {
					id: '',
					title: '',
					url: '',
					type: '',
					state: '',
					types: [{
							value: 'word',
							label: '文字'
						},
						{
							value: 'url',
							label: '链接'
						}
					],
				},
				formcomp: {
					id: '',
					state: '',
					title: '',
					url: '',
					imageUrl: ''
				}
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
			},
			handleAvatarSuccess3(file) {
				this.formcomp.imageUrl = file.data;
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.size = val;
				this.homeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.homeSelect()
			},
			handleSizeChange2(val) {
				this.size = val;
				this.upgradeSelect()
			},
			handleCurrentChange2(val) {
				this.page = val;
				this.upgradeSelect()
			},
			handleSizeChange3(val) {
				this.size = val;
				this.choicenSelect()
			},
			handleCurrentChange3(val) {
				this.page = val;
				this.choicenSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '首页公告') {
					this.page = 1
					this.limit = 5
					this.state = 1
					this.dataSelect()
				}
				if (tab._props.label == '首页导航') {
					this.page = 1
					this.limit = 5
					this.nav = 1
					this.homeSelect()
				}
				if (tab._props.label == '首页推广') {
					this.page = 1
					this.limit = 5
					this.nav = 2
					this.homeSelect()
				}
				if (tab._props.label == '升级配置') {
					this.page = 1
					this.limit = 5
					this.upgradeSelect()
				}
				if (tab._props.label == '精选商品') {
					this.page = 1
					this.limit = 5
					this.choicenSelect()
					this.handpick()
				}
				if (tab._props.label == '首页轮播') {
					this.page = 1
					this.limit = 5
					this.bannerSelect()
				}
			},
			handleAvatarSuccess(file) {
				this.imageUrl = file.data;
			},
			handleAvatarSuccess2(file2) {
				this.form1.imageUrl = file2.data;
			},
			// 添加
			addNoticelun() {
				this.dialogFormVisible6 = true
			},
			// 添加轮播图
			releaslunNoticeTo() {
				if (this.url == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '链接不能为空',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl(`/banner/update/-1?url=${this.url}&image=${this.imageUrl}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible6 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.bannerSelect()
							this.url = ''
							this.imageUrl = ''
						}
					})
				})
			},
			// 编辑弹框
			compilelun(item) {
				this.dialogFormVisible7 = true
				this.form1.url = item.url
				this.form1.imageUrl = item.imageUrl
				this.form1.id = item.id
			},
			// 编辑轮播
			upgradbianTo() {
				this.$http({
					url: this.$http.adornUrl(
						`/banner/update/${this.form1.id}?url=${this.form1.url}&image=${this.form1.imageUrl}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible7 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.bannerSelect()
						}
					})
				})
			},
			// 删除轮播
			deleteslun(id) {
				this.id = id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`/banner/delete/${this.id}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = '';
								this.bannerSelect()
							}
						})
					})
				}).catch(() => {})
			},
			//轮播数据
			bannerSelect() {
				this.$http({
					url: this.$http.adornUrl('/banner/'),
					method: 'get',
					data: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.bannerData = returnData
				})
			},
			// 添加公告弹框
			addNotice() {
				this.dialogFormVisible = true
			},
			// 添加公告
			releasgongaoTo() {
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择公告标题',
						type: 'warning'
					});
					return
				}
				if (this.url == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择公告链接',
						type: 'warning'
					});
					return
				}
				if (this.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择公告类型',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('/ActivityMessage/add'),
					method: 'post',
					data: this.$http.adornData({
						'state': this.state,
						'url': this.url,
						'title': this.title,
						'type': this.type
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
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
			// 修改弹框
			updates(index, rows) {
				this.form.state = rows.state;
				this.dialogFormVisible1 = true;
				this.form.id = rows.id;
				this.form.title = rows.title;
				this.form.url = rows.url;
				this.form.type = rows.type;
			},
			// 修改商品类别
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('/ActivityMessage/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'state': this.form.state,
						'title': this.form.title,
						'url': this.form.url,
						'type': this.form.type,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible1 = false
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
			// 删除公告
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`/ActivityMessage/delete/${row.id}`),
						method: 'get',
						params: this.$http.adornParams({})
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
			// 修改弹框
			compile(index, rows) {
				this.dialogFormVisible2 = true;
				this.formcomp.id = rows.id;
				this.formcomp.state = rows.state;
				this.formcomp.title = rows.title;
				this.formcomp.url = rows.url;
				this.formcomp.imageUrl = rows.imageUrl;
			},
			// 修改商品类别
			compileNoticeTo() {
				this.$http({
					url: this.$http.adornUrl(`/activity/update/${this.formcomp.id}`),
					method: 'post',
					data: this.$http.adornData({
						'id': this.formcomp.id,
						'state': this.formcomp.state,
						'title': this.formcomp.title,
						'url': this.formcomp.url,
						'imageUrl': this.formcomp.imageUrl,
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
							this.homeSelect()
						}
					})
				})
			},
			// 添加升级弹框
			addUpgrade() {
				this.dialogFormVisible3 = true
			},
			// 添加升级
			upgradaddTo() {
				if (this.wgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入统一地址',
						type: 'warning'
					});
					return
				}
				if (this.androidWgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入安卓地址',
						type: 'warning'
					});
					return
				}
				if (this.iosWgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入苹果地址',
						type: 'warning'
					});
					return
				}

				if (this.version == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入版本号',
						type: 'warning'
					});
					return
				}
				if (this.method == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择升级方式',
						type: 'warning'
					});
					return
				}
				if (this.des == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入升级内容',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('/appinfo/save'),
					method: 'post',
					data: this.$http.adornData({
						'androidWgtUrl': this.androidWgtUrl,
						'iosWgtUrl': this.iosWgtUrl,
						'wgtUrl': this.wgtUrl,
						'version': this.version,
						'method': this.method,
						'des': this.des
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible3 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.upgradeSelect()
						}
					})
				})
			},
			// 修改升级弹框
			upgradebj(index, rows) {
				this.dialogFormVisible4 = true;
				this.formupgrad.androidWgtUrl = rows.androidWgtUrl;
				this.formupgrad.id = rows.id;
				this.formupgrad.iosWgtUrl = rows.iosWgtUrl;
				this.formupgrad.wgtUrl = rows.wgtUrl;
				this.formupgrad.version = rows.version;
				this.formupgrad.des = rows.des;
				this.formupgrad.method = rows.method;
			},
			// 修改升级
			upgradbjTo() {
				this.$http({
					url: this.$http.adornUrl('/appinfo/save'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.formupgrad.id,
						'androidWgtUrl': this.formupgrad.androidWgtUrl,
						'iosWgtUrl': this.formupgrad.iosWgtUrl,
						'wgtUrl': this.formupgrad.wgtUrl,
						'version': this.formupgrad.version,
						'des': this.formupgrad.des,
						'method': this.formupgrad.method,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible4 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.upgradeSelect()
						}
					})
				})
			},
			// 删除升级
			upgradelete(row) {
				let delid = row.id
				this.id = delid
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`/appinfo/delete/${this.id}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = '';
								this.upgradeSelect()
							}
						})
					})
				}).catch(() => {})
			},
			//添加精选商品
			choiaddNotice() {
				this.dialogFormVisible5 = true
			},
			// 添加商品
			releasNoticeTo(row) {
				this.$http({
					url: this.$http.adornUrl('/commodity/save'),
					method: 'post',
					data: this.$http.adornData({
						'activityid': row.activityid,
						'couponendtime': row.couponendtime,
						'couponmoney': row.couponmoney,
						'couponstarttime': row.couponstarttime,
						'couponurl': row.couponurl,
						'deposit': row.deposit,
						'deposit_deduct': row.deposit_deduct,
						'item_from': row.item_from,
						'itemdesc': row.itemdesc,
						'itemendprice': row.itemendprice,
						'itemid': row.itemid,
						'itempic': row.itempic,
						'itemprice': row.itemprice,
						'itemsale': row.itemsale,
						'itemshorttitle': row.itemshorttitle,
						'itemtitle': row.itemtitle,
						'presale_end_time': row.presale_end_time,
						'presale_start_time': row.presale_start_time,
						'presale_tail_end_time': row.presale_tail_end_time,
						'presale_tail_start_time': row.presale_tail_start_time,
						'sellernick': row.sellernick,
						'shopname': row.shopname,
						'shoptype': row.shoptype,
						'taobao_image': row.taobao_image,
						'tkrates': row.tkrates,
						'videoid': row.videoid,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.choicenSelect()
								this.keyword = ''
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 精选淘宝商品
			handpick() {
				this.tableDataLoading = true
				this.keyword = '网红'
				this.$http({
					url: this.$http.adornUrl(
						`supersearch/apikey/maxd/back/20/sort/2/keyword/${this.keyword}/is_coupon/1/tb_p/1/min_id/1`
						),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data && data.code === 1) {
						this.tableDataLoading = false
						this.keyword = ''
						let returnData = data.data;
						this.choicenData2 = returnData
					}
				})
			},
			// 搜索
			shousuo() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(
						`supersearch/apikey/maxd/back/100/sort/2/keyword/${this.keyword}/is_coupon/1/tb_p/1/min_id/1`
						),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					console.log(data)
					if (data && data.code === 1) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.choicenData2 = returnData
					}
				})
			},
			// 批量删除商品
			choideletes(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				for (var i in ids) {
					this.$http({
						url: this.$http.adornUrl(`/commodity/deleteById?id=${ids[i]}`),
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
								this.choicenSelect()
							}
						})
					})
				}
			},
			// 删除商品
			choidelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`/commodity/deleteById/?id=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.choicenSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`/ActivityMessage/page/${this.state}/${this.page}/${this.limit}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 获取首页数据列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`/activity/state/${this.nav}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.homeData = returnData
				})
			},
			// 升级配置数据列表
			upgradeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('/appinfo/'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.upgradeData = returnData
				})
			},
			// 精选商品列表
			choicenSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('/commodity/selectCommodityList'),
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
					this.choicenData = returnData
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style>
	.customWidth {
		width: 80% !important;
	}

	.adver_main.box {
		display: block;
		max-width: 100%;
		text-align: center;
		border: 1px dotted rgba(67, 79, 103, .4);
	}

	.cards {
		padding: 0 8px;
		margin-bottom: 15px;
	}

	.adver_main.box a {
		display: flex;
		justify-content: center;
		height: 150px;
		line-height: 150px;
		text-decoration: none
	}

	.bannerManin {
		border: 1px solid #e8e8e8;
		font-size: 14px;
		padding: 0 24px;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 113px;
		color: rgba(0, 0, 0, .65);
	}

	.bannerManin span {
		display: inline-block;
		margin-left: 5px;
	}

	.bannerManin img {
		width: 48px;
		height: 48px;
		border-radius: 50%;
	}

	.bannerbtn {
		display: flex;
		border-top: none !important;
		border: 1px solid #e8e8e8;
		padding: 11px;
		font-size: 14px;
		color: #3E8EF7;
	}

	.bannerbtn a {
		flex: 1;
		text-align: center;
		color: #3E8EF7 !important;
		text-decoration: none;
	}
</style>
