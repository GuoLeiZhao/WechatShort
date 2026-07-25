<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="排行榜管理" name="first">
				<div style = "float: right;margin-right:2%;">
					<el-button style = "margin: 10px 0;" :disabled = "!isAuth('landingPage:addRank')" size = "mini" type = "primary" icon = "document"
							   @click = "showAddRank">添加排行榜</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column fixed prop="id" label="编号" width="80"/>
					<el-table-column fixed prop="name" label="短剧名称" width="150"/>
					<el-table-column label="封面">
						<template slot-scope="scope">
							<span v-if="scope.row.pic==null"> - </span>
							<img v-else :src="scope.row.pic" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="groupName" label="所属平台">
						<template slot-scope="scope">
							<span>
								{{ scope.row.groupName ? scope.row.groupName : ' - ' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="appId" label="小程序 AppId"></el-table-column>
					<el-table-column prop="url" label="分享链接" show-overflow-tooltip>
						<template slot-scope="scope">
							<el-tag v-if="scope.row.url" type="success" size="mini" @click="copyText(scope.row.url)" style="cursor: pointer;" >
								复制
							</el-tag>
							{{ scope.row.url}}
						</template>
					</el-table-column>
					<el-table-column prop="type" label="链接类型">
						<template slot-scope="scope">
							<span v-if="scope.row.type == null"> - </span>
							<span v-if="scope.row.type === 'private'">私包</span>
							<span v-if="scope.row.type === 'public'">公包</span>
						</template>
					</el-table-column>
					<el-table-column prop="score" label="星级">
						<template slot-scope="scope">
							<span>{{ scope.row.score ? scope.row.score + ' 星' : '-' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="description" label="短剧描述" show-overflow-tooltip>
						<template slot-scope="scope">
							<span>{{ scope.row.description ? scope.row.description : '-' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sort" label="排序">
						<template slot-scope="scope">
							<span>{{ scope.row.sort ? scope.row.sort : '0' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createdAt" label="创建时间" width="160"></el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="warning" @click="showUpdateRank(scope.$index, scope.row, true)"
									   :disabled="!isAuth('landingPage:updateRank')">编辑</el-button>
<!--							<el-button size="mini" type="primary" @click="showUpdateRank(scope.$index, scope.row, false)">详情</el-button>-->
							<el-button size="mini" type="danger" :disabled="!isAuth('landingPage:deleteRank')"
									   @click="deleteRank(scope.row)">删除</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								   :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								   layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
					</el-pagination>
				</div>
				<!-- 添加弹框 -->
				<el-dialog title = "添加：" :visible.sync = "rankDialogAddFormVisible" center>
					<el-form :model="addData" :rules="rankRules" ref="addRankForm" label-width="200px">
						<el-form-item label="短剧名称" prop="name">
							<el-input style = "width:50%;" v-model = "addData.name" placeholder = "请输入短剧名称"/>
						</el-form-item>
						<el-form-item label="封面：" prop="pic">
							<el-upload class = "avatar-uploader" v-model = "addData.pic"
									   :action="$http.adornUrl('alioss/upload')"
									   :show-file-list = "false"
									   :on-success = "handleRankPicSuccess"
									   style = "width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;"
							>
								<img v-if = "addData.pic" :src = "addData.pic" class = "avatar"
									 style = "border-radius: 6px;width: 148px;height: 148px;"/>
								<i v-else class = "el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</el-form-item>
						<!-- 短剧描述 -->
						<el-form-item label="短剧描述：" prop="description">
							<div  style = "width:50%;">
								<el-input type="textarea" :autosize="{ minRows: 2, maxRows: 6}"
										  v-model="addData.description"/>
							</div>
						</el-form-item>
						<!-- 所属平台 + 小程序 AppId 绑定 -->
						<el-form-item label="所属平台：" prop="groupAppId">
							<el-select v-model="addData.groupAppId" placeholder="请选择" value="">
								<el-option
									v-for="item in appOptions"
									:key="item.value"
									:label="item.label"
									:value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="分享链接：" prop="url">
							<el-input style = "width:50%;" v-model = "addData.url" placeholder = "请输入分享链接"></el-input>
						</el-form-item>
						<!-- 链接类型 -->
						<el-form-item label="链接类型：" prop="type">
							<el-select v-model="addData.type" placeholder="请选择" value="">
								<el-option
									v-for="item in urlTypeOptions"
									:key="item.value"
									:label="item.label"
									:value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<!-- 星级 -->
						<el-form-item label="评分：" prop="score">
							<el-input-number v-model="addData.score" :min="1" :max="10" label="评分"/>
						</el-form-item>
						<el-form-item label="排序：" prop="sort">
							<el-input style = "width:50%;" v-model = "addData.sort" placeholder = "请输入排序" ></el-input>
						</el-form-item>
						<el-form-item>
							<el-button type = "primary" @click = "addRank()">立即创建</el-button>

							<el-button style="margin-left: 20px !important;" @click = "rankDialogAddFormVisible = false">取 消</el-button>
						</el-form-item>
					</el-form>
				</el-dialog>
				<el-dialog :title = "updateRankTitle" :visible.sync = "rankDialogModifyFormVisible" center>
					<div style = "margin-bottom: 10px;">
						<span style = "width: 200px;display: inline-block;text-align: right;">短剧名称：</span>
						<el-input style = "width:50%;" v-model = "updateData.name" placeholder = "请输入短剧名称"></el-input>
					</div>
					<div style = "margin-bottom: 10px;display: flex;">
						<span style = "width: 200px;display: inline-block;text-align: right;">封面：</span>
						<div
							style = "width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
							<el-upload class = "avatar-uploader" v-model = "updateData.pic"
									   :action="$http.adornUrl('alioss/upload')"  :show-file-list = "false"
									   :on-success = "res => updateData.pic = res.data">
								<img v-if = "updateData.pic" :src = "updateData.pic" class = "avatar"
									 style = "border-radius: 6px;width: 148px;height: 148px;"/>
								<i v-else class = "el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</div>
					</div>
					<!-- 短剧描述 -->
					<div style = "margin-bottom: 10px; display: flex">
						<span style = "width: 200px;display: inline-block;text-align: right;align-items: center">短剧描述：</span>
						<div  style = "width:50%;">
							<el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}"
									  v-model="updateData.description"/>
						</div>
					</div>
					<!-- 所属平台 + 小程序 AppId 绑定 -->
					<div style = "margin-bottom: 10px;">
						<span style = "width: 200px;display: inline-block;text-align: right;">所属平台：</span>
						<el-select v-model="updateData.groupAppId" placeholder="请选择">
							<el-option
								v-for="item in appOptions"
								:key="item.value"
								:label="item.label"
								:value="item.value">
							</el-option>
						</el-select>
					</div>
					<div style = "margin-bottom: 10px;">
						<span style = "width: 200px;display: inline-block;text-align: right;">分享链接：</span>
						<el-input style = "width:50%;" v-model = "updateData.url" placeholder = "请输入分享链接"></el-input>
					</div>
					<!-- 链接类型 -->
					<div style = "margin-bottom: 10px;">
						<span style = "width: 200px;display: inline-block;text-align: right;">链接类型：</span>
						<el-select v-model="updateData.type" placeholder="请选择">
							<el-option
								v-for="item in urlTypeOptions"
								:key="item.value"
								:label="item.label"
								:value="item.value">
							</el-option>
						</el-select>
					</div>
					<!-- 星级 -->
					<div style = "margin-bottom: 10px;">
						<span style = "width: 200px;display: inline-block;text-align: right;">评分：</span>
						<el-input-number v-model="updateData.score" :min="1" :max="10" label="评分"/>
					</div>
					<div style = "margin-bottom: 10px;">
						<span style = "width: 200px;display: inline-block;text-align: right;">排序：</span>
						<el-input style = "width:50%;" v-model = "updateData.sort" placeholder = "请输入排序" ></el-input>
					</div>
					<div slot = "footer" class = "dialog-footer">
						<el-button @click = "rankDialogModifyFormVisible = false">取 消</el-button>
						<el-button type = "primary" @click = "updateRank()">确 定</el-button>
					</div>
				</el-dialog>

			</el-tab-pane>
			<el-tab-pane label="分享卡片管理" name="second">
				<div style = "float: right;margin-right:2%;">
					<el-button style = "margin: 10px 0;" :disabled = "!isAuth('landingPage:addShare')" size = "mini" type = "primary" icon = "document"
							   @click = "shareDialogAddFormVisible = true">添加分享页</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column fixed prop="id" label="编号" width="80"/>
					<el-table-column fixed prop="name" label="短剧名称" width="150"/>
					<el-table-column label="封面" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.pic==null"> - </span>
							<img v-else :src="scope.row.pic" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="groupName" label="所属平台" width="100">
						<template slot-scope="scope">
							<span>
								{{ scope.row.groupName ? scope.row.groupName : ' - ' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="url" label="短剧分享链接" show-overflow-tooltip>
						<template slot-scope="scope">
							<el-tag v-if="scope.row.url" type="success" size="mini" @click="copyText(scope.row.url)" style="cursor: pointer;" >
								复制
							</el-tag>
							{{ scope.row.url ? scope.row.url : ' - ' }}
						</template>
					</el-table-column>
					<el-table-column prop="url" label="落地页链接" show-overflow-tooltip width="500">
						<template slot-scope="scope">
							<span v-if="scope.row.type === 'normal'">
								<el-tag type="success" size="mini" @click="copyText('/pages/navigator/navigator?id='+ scope.row.id)" style="cursor: pointer;" >
									复制
								</el-tag>
								/pages/navigator/navigator?id={{ scope.row.id }}
							</span>
							<span v-if="scope.row.type === 'image'">
								<el-tag type="success" size="mini" @click="copyText('/pages/navigator/navigatorImg?id='+ scope.row.id)" style="cursor: pointer;" >
									复制
								</el-tag>
								/pages/navigator/navigatorImg?id={{ scope.row.id }}
							</span>
							<span v-if="scope.row.type === 'qrcode'">
								<el-tag type="success" size="mini" @click="copyText('/pages/navigator/navigatorQrCode?id='+ scope.row.id)" style="cursor: pointer;" >
									复制
								</el-tag>
								/pages/navigator/navigatorQrCode?id={{ scope.row.id }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="remark" label="备注" show-overflow-tooltip width="200">
						<template slot-scope="scope">
							<span>{{ scope.row.remark ? scope.row.remark : '-' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createdAt" label="创建时间" width="160"></el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="warning" @click="showUpdateShare(scope.$index, scope.row, true)"
									   :disabled="!isAuth('landingPage:updateShare')">编辑</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('landingPage:deleteShare')"
									   @click="deleteShare(scope.row)">删除</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleShareSizeChange" @current-change="handleShareCurrentChange"
								   :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								   layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
					</el-pagination>
				</div>
				<!-- 添加弹框 -->
				<el-dialog title = "添加：" :visible.sync = "shareDialogAddFormVisible" center>
					<el-form :model="addShareData" :rules="shareRules" ref="addShareForm" label-width="200px">
						<el-form-item label="短剧名称" prop="name">
							<el-input style = "width:50%;" v-model = "addShareData.name" placeholder = "请输入短剧名称"/>
						</el-form-item>
						<el-form-item label="卡片类型">
							<el-radio-group v-model="addShareData.type">
								<el-radio label="normal">普通</el-radio>
								<el-radio label="image">图文</el-radio>
								<el-radio label="qrcode">企微引流</el-radio>
							</el-radio-group>
						</el-form-item>
						<el-form-item label="封面：" prop="pic">
							<el-upload class = "avatar-uploader" v-model = "addShareData.pic"
									   :action="$http.adornUrl('alioss/upload')"
									   :show-file-list = "false"
									   :on-success = "res => this.addShareData.pic = res.data"
									   style = "width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;"
							>
								<img v-if="addShareData.pic" :src = "addShareData.pic" class = "avatar" style = "border-radius: 6px;width: 148px;height: 148px;"/>
								<i v-else class = "el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</el-form-item>
						<!-- 所属平台 + 小程序 AppId 绑定 -->
						<el-form-item v-if="addShareData.type !== 'qrcode'"  label="所属平台：" prop="groupAppId">
							<el-select v-model="addShareData.groupAppId" placeholder="请选择" value="">
								<el-option
									v-for="item in appOptions"
									:key="item.value"
									:label="item.label"
									:value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item v-if="addShareData.type !== 'qrcode'" label="分享链接：" prop="url">
							<el-input style = "width:50%;" v-model = "addShareData.url" placeholder = "请输入分享链接"></el-input>
						</el-form-item>
						<el-form-item label="备注：" prop="sort">
							<el-input style = "width:50%;" v-model = "addShareData.remark" placeholder = "请输入备注" ></el-input>
						</el-form-item>
						<el-form-item>
							<el-button type = "primary" @click = "addShare()">立即创建</el-button>
							<el-button style="margin-left: 20px !important;" @click = "shareDialogAddFormVisible = false">取 消</el-button>
						</el-form-item>
					</el-form>
				</el-dialog>
				<!-- 更新弹窗 -->
				<el-dialog :title = "updateShareTitle" :visible.sync = "shareDialogModifyFormVisible" center>
					<el-form :model="updateShareData" :rules="shareRules" ref="updateShareForm" label-width="200px">
						<el-form-item label="短剧名称：">
							<el-input style = "width:50%;" v-model = "updateShareData.name" placeholder = "请输入短剧名称"></el-input>
						</el-form-item>
						<el-form-item label="卡片类型">
							<el-radio-group v-model="updateShareData.type">
								<el-radio label="normal">普通</el-radio>
								<el-radio label="image">图文</el-radio>
								<el-radio label="qrcode">企微引流</el-radio>
							</el-radio-group>
						</el-form-item>
						<el-form-item label="封面：">
							<el-upload class = "avatar-uploader" v-model = "updateShareData.pic"
									   :action="$http.adornUrl('alioss/upload')"  :show-file-list = "false"
									   :on-success = "res => this.updateShareData.pic = res.data"
									   style = "width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;"
							>
								<img v-if = "updateShareData.pic" :src = "updateShareData.pic" class = "avatar"
									 style = "border-radius: 6px;width: 148px;height: 148px;"/>
								<i v-else class = "el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</el-form-item>
						<!-- 所属平台 + 小程序 AppId 绑定 -->
						<el-form-item label="所属平台：">
							<el-select v-model="updateShareData.groupAppId" placeholder="请选择">
								<el-option
									v-for="item in appOptions"
									:key="item.value"
									:label="item.label"
									:value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="分享链接：">
							<el-input style = "width:50%;" v-model = "updateShareData.url" placeholder = "请输入分享链接"></el-input>
						</el-form-item>
						<el-form-item label="备注：">
							<el-input style = "width:50%;" v-model = "updateShareData.remark" placeholder = "备注" ></el-input>
						</el-form-item>
						<el-form-item>
							<el-button @click = "shareDialogModifyFormVisible = false">取 消</el-button>
							<el-button type = "primary" @click = "updateShare()">确 定</el-button>
						</el-form-item>
					</el-form>
				</el-dialog>
			</el-tab-pane>
		</el-tabs>
	</div>
</template>
<script>
export default {
	name: "landingPage",
	data() {
		return {
			state: 'false',
			limit: 10,
			page: 1,
			tableDataLoading: true,
			tableData: [],
			activeName: 'first',
			rankDialogAddFormVisible: false,
			rankDialogModifyFormVisible: false,
			rankDialogEditable: false,
			shareDialogAddFormVisible: false,
			shareDialogModifyFormVisible: false,
			shareDialogEditable: false,
			updateRankTitle: '详情',
			addData: {
				pic: "",
			},
			updateData:{},
			rankRules:{
				name: [
					{ required: true, message: '请输入短剧名称', trigger: 'blur' },
					{ max: 32, message: '长度在 32 个字符以内', trigger: 'blur' }
				],
				pic: [
					{ required: true, message: '请上传封面', trigger: 'blur' },
				],
				description: [
					{ required: true, message: '请输入短剧描述', trigger: 'blur' },
					{ max: 255, message: '长度在 255 个字符以内', trigger: 'blur' }
				],
				groupAppId: [
					{ required: true, message: '请选择所属平台', trigger: 'blur' },
				],
				url: [
					{ required: true, message: '请输入分享链接', trigger: 'blur' },
				],
				type: [
					{ required: true, message: '请选择链接类型', trigger: 'blur' },
				],
				score: [
					{ required: true, message: '请选择评分', trigger: 'blur' },
					{ type: 'number', message: '年龄必须为数字值'}
				],
				sort: [
					{ type: 'number', message: '排名必须为数字值'}
				],
			},

			updateShareTitle: '详情',
			addShareData: {
				pic: "",
				type: "normal",
			},
			updateShareData: {},
			shareRules:{
				name: [
					{ required: true, message: '请输入短剧名称', trigger: 'blur' },
					{ max: 32, message: '长度在 32 个字符以内', trigger: 'blur' }
				],
				pic: [
					{ required: true, message: '请上传封面', trigger: 'blur' },
				],
				groupAppId: [
					{ required: true, message: '请选择所属平台', trigger: 'blur' },
				],
				url: [
					{ required: true, message: '请输入分享链接', trigger: 'blur' },
				],
			},
			appOptions: [
				{
					label : "小程序1",
					value: "小程序1:__WX_APPID_CANDLE__"
				},
				{
					label : "小程序2",
					value: "小程序2:__WX_APPID_HANCHAN__"
				},
				{
					label : "小程序3",
					value: "小程序3:__WX_APPID_QINGFANG__"
				},
				{
					label : "小程序4",
					value: "小程序4:__WX_APPID_NINGQI__"
				},
				{
					label : "小程序5",
					value: "小程序5:__WX_APPID_YUHAO__"
				},
				{
					label : "小程序6",
					value: "小程序6:__WX_APPID_RANK__"
				},
				{
					label : "小程序7",
					value: "小程序7:__WX_APPID_MINGYUN__"
				},
				{
					label : "小程序8",
					value: "小程序8:__WX_APPID_SHUIGUO__"
				},
			],
			urlTypeOptions: [
				{
					label : "自有小程序",
					value: "own"
				},
				{
					label : "公包",
					value: "public"
				},
				{
					label : "私包",
					value: "private"
				},
			],
		}
	},
	methods: {
		// tabs切换
		handleClick(tab, event) {
			this.page = 1
			if (tab._props.label == '排行榜管理') {
				this.dataSelect()
			}
			if (tab._props.label == '分享卡片管理') {
				this.dataShareSelect()
			}

		},
		showAddRank(){
			this.rankDialogAddFormVisible = true
			if (Object.keys(this.addData).length === 0){
				this.addData.sort = 1
				this.addData.score = 5
			}
		},
		addRank(){
			this.$refs['addRankForm'].validate((valid) => {
				if (valid) {
					const groupAppId = this.addData.groupAppId;
					if (groupAppId){
						this.addData.groupName = groupAppId.split(':')[0]
						this.addData.appId = groupAppId.split(':')[1]
					}
					this.$http({
						url: this.$http.adornUrl('videoLandingPage'),
						method: 'post',
						data: this.$http.adornData(this.addData)
					}).then(res => {
						this.rankDialogAddFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.addData = {}
								this.dataSelect()
							}
						})
					})
				} else {
					return false;
				}
			});
		},
		// 详情跳转
		showUpdateRank(index, row, editable) {
			this.rankDialogModifyFormVisible = true
			this.rankDialogEditable = editable
			this.updateRankTitle = editable ? '编辑' : '详情'
			this.updateData = { ...row }
			console.log(row)
		},
		// 详情跳转
		updateRank() {
			const groupAppId = this.updateData.groupAppId;
			if (groupAppId){
				this.updateData.groupName = groupAppId.split(':')[0]
				this.updateData.appId = groupAppId.split(':')[1]
			}
			this.$http({
				url: this.$http.adornUrl('videoLandingPage'),
				method: 'put',
				data: this.$http.adornData(this.updateData)
			}).then(res => {
				this.rankDialogModifyFormVisible = false
				this.$message({
					message: '操作成功',
					type: 'success',
					duration: 1500,
					onClose: () => {
						this.updateData = {}
						this.dataSelect()
					}
				})
			})
		},
		// 上传照片成功
		handleRankPicSuccess(file) {
			this.addData.pic = file.data
		},
		handleSizeChange(val) {
			this.limit = val
			this.dataSelect()
		},
		handleCurrentChange(val) {
			this.page = val
			this.dataSelect()
		},
		//删除用户
		deleteRank(row) {
			let id = row.id
			this.$confirm(`确定删除此条信息?`, '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$http({
					url: this.$http.adornUrl(`videoLandingPage/${id}`),
					method: 'delete',
					params: this.$http.adornData({})
				}).then(({
							 data
						 }) => {
					if (data.code === 0) {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			})
		},

		// 获取数据列表
		dataSelect() {
			this.tableDataLoading = true
			this.$http({
				url: this.$http.adornUrl('videoLandingPage/selectPage'),
				method: 'get',
				params: this.$http.adornParams({
					'currPage': this.page,
					'pageSize': this.limit,
				})
			}).then(({data}) => {
				this.tableDataLoading = false
				this.tableData = data.data
				this.tableData.records.forEach(item => {
					item.groupAppId = item.groupName + ':' + item.appId
				})
				console.log(this.tableData)
			})
		},

		handleShareSizeChange(val) {
			this.limit = val
			this.dataShareSelect()
		},
		handleShareCurrentChange(val) {
			this.page = val
			this.dataShareSelect()
		},




		addShare(){
			this.$refs['addShareForm'].validate((valid) => {
				if (valid) {
					const groupAppId = this.addShareData.groupAppId;
					if (groupAppId){
						this.addShareData.groupName = groupAppId.split(':')[0]
						this.addShareData.appId = groupAppId.split(':')[1]
					}
					this.$http({
						url: this.$http.adornUrl('videoLandingPageShare'),
						method: 'post',
						data: this.$http.adornData(this.addShareData)
					}).then(res => {
						this.shareDialogAddFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.addShareData = {
									type: "normal",
								}
								this.dataShareSelect()
							}
						})
					})
				} else {
					return false;
				}
			});
		},


		showUpdateShare(index, row, editable) {
			this.shareDialogModifyFormVisible = true
			this.shareDialogEditable = editable
			this.updateShareTitle = editable ? '编辑' : '详情'
			this.updateShareData = { ...row }
		},

		// 详情跳转
		updateShare() {
			this.$refs['updateShareForm'].validate((valid) => {
				if (valid) {
					const groupAppId = this.updateShareData.groupAppId;
					if (groupAppId){
						this.updateShareData.groupName = groupAppId.split(':')[0]
						this.updateShareData.appId = groupAppId.split(':')[1]
					}
					this.$http({
						url: this.$http.adornUrl('videoLandingPageShare'),
						method: 'put',
						data: this.$http.adornData(this.updateShareData)
					}).then(res => {
						this.shareDialogModifyFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.updateShareData = {}
								this.dataShareSelect()
							}
						})
					})
				} else {
					return false;
				}
			});
		},

		//删除用户
		deleteShare(row) {
			let id = row.id
			this.$confirm(`删除后，分享后的落地页链接将会失效，请谨慎操作！`, '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$http({
					url: this.$http.adornUrl(`videoLandingPageShare/${id}`),
					method: 'delete',
					params: this.$http.adornData({})
				}).then(({
							 data
						 }) => {
					if (data.code === 0) {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataShareSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			})
		},
		dataShareSelect(){
			this.tableDataLoading = true
			this.$http({
				url: this.$http.adornUrl('videoLandingPageShare/selectPage'),
				method: 'get',
				params: this.$http.adornParams({
					'currPage': this.page,
					'pageSize': this.limit,
				})
			}).then(({data}) => {
				this.tableDataLoading = false
				this.tableData = data.data
				this.tableData.records.forEach(item => {
					item.groupAppId = item.groupName + ':' + item.appId
				})
				console.log(data)
			})
		},
	},
	mounted() {
		this.dataSelect()
	}
}
</script>

<style scoped="scoped">
.el-button+.el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>
