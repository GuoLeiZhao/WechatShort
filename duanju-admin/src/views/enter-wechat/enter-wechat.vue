<template>
	<div>
		<div style = "float: right;margin-right:2%;">
			<el-button style = "margin: 10px 0;" :disabled = "!isAuth('enter-wechat:add')" size = "mini" type = "primary" icon = "document"
					   @click = "showAddMsgDialog">添加企业群发</el-button>
		</div>
		<div>
			<el-table empty-text=" - " v-loading = "tableDataLoading" :data = "tableData.groupMsgList">
				<el-table-column type = "index" label = "编号" width = "80" align="center">
				</el-table-column>
				<el-table-column prop = "createType" label = "创建来源" width = "100" align="center">
					<template slot-scope="scope">
						{{ scope.row.createType === 1 ? '个人' : '企业'}}
					</template>
				</el-table-column>
				<el-table-column prop = "text.content" label = "文本内容"/>
				<el-table-column prop = "createTime" label = "创建时间" align="center" width = "250" >
					<template slot-scope="scope">{{ parseInt(scope.row.createTime) * 1000 | dateFormat("YYYY-MM-DD HH:mm:ss")}}</template>
				</el-table-column>
				<el-table-column label = "操作" width = "150">
					<template slot-scope = "scope">
						<el-button size = "mini" type = "primary" :disabled = "!isAuth('message:update')" @click = "updates(scope.$index, scope.row)">查看
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-button style="width:100%;height:50px;background:#fff;color:#000" icon="el-icon-refresh" @click="loadMore" type="primary">加载更多...</el-button>
		</div>
		<!-- 添加弹框 -->
		<el-dialog title = "创建群发任务" :visible.sync = "addMsgDialogVisible" :close-on-click-modal="false" center>
			<el-form ref="form" :model="addMsgForm" label-width="150px">
				<el-form-item label="群发任务类型">
					<el-radio-group v-model="addMsgForm.chatType">
						<el-radio label="single">发送客户</el-radio>
						<el-radio label="group" disabled>发送客户群</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="发送人群">
					<el-radio-group v-model="addMsgForm.sendType">
						<el-radio label="all" disabled>全部客户</el-radio>
						<el-radio label="label">按照标签筛选客户</el-radio>
					</el-radio-group>
					<el-checkbox-group
						v-if="'label' === addMsgForm.sendType"
						v-for="(item, idx) in tagGroup"
						:key="idx"
						v-model="selectedTag"
					>
						<el-tag type="info" style="width: 100%; text-align: center !important;">{{ item.groupName }}</el-tag>
						<el-checkbox v-for="(tagItem) in item.tag"
									 :label="item.groupName + ':' + tagItem.id"
									 :key="tagItem.id">
							{{tagItem.name}}
						</el-checkbox>
					</el-checkbox-group>
				</el-form-item>
				<el-form-item label="文本内容" :rules="[{ max: 4000, message: '长度在 4000 个字符以内', trigger: 'blur' }]">
					<el-input style="width: 90%;" type="textarea" :autosize="{ minRows: 3, maxRows: 8}" v-model="addMsgForm.text.content"/>
				</el-form-item>
				<el-form-item label="附件">
					<el-button type = "primary" @click = "addAttachment">添加附件</el-button>
				</el-form-item>
				<el-form-item
					v-for="(item, idx) in dynamicAttachment"
					:label="'附件'+ (idx + 1)"
					:key="idx"
				>
					<el-radio-group v-model="item.msgtype">
						<el-radio label="miniprogram">小程序</el-radio>
						<el-radio label="link">链接</el-radio>
						<el-radio label="image">图片</el-radio>
						<el-radio label="video">视频</el-radio>
						<el-radio label="file">文件</el-radio>
					</el-radio-group>
					<el-button icon="el-icon-delete" style="position: relative;left: 50%" size="mini" @click="removeAttachment(idx)" circle/>
					<el-form-item v-if="'miniprogram' === item.msgtype" style="margin-top: 22px">
						<el-form-item label="标题" label-width="46px" :rules="[{ max: 64, message: '长度在 64 个字符以内', trigger: 'blur' }]">
							<el-input style="width: 90%" v-model="item.miniprogram.title"></el-input>
						</el-form-item>
						<el-form-item label="图片" label-width="46px" style="margin-top: 10px">
							<el-upload class = "avatar-uploader"
									   :show-file-list = "false"
									   :action="$http.adornUrl('enterpriseWechat/tempMediaUpload/image')"
									   :on-success="res => handleMiniSuccess(res, item, idx)"
									   :before-upload="beforeMiniUpload"
									   :multiple="false"
									   accept="image/*"
									   style = "margin-top: 10px; width:148px; height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;"
							>
								<img v-if="item.miniprogram.picMediaId" :src = "$http.adornUrl('enterpriseWechat/getTempMedia/'+item.miniprogram.picMediaId)"
									 style = "border-radius: 6px;width: 148px;height: 148px;"/>
								<i v-else class = "el-icon-plus avatar-uploader-icon" style = "width: 148px;height: 148px;"></i>
							</el-upload>
						</el-form-item>
						<el-form-item label="app" label-width="46px" style="margin-top: 15px">
							<el-select v-model="item.miniprogram.appid" placeholder="请选择" value="">
								<el-option
									v-for="app_item in appOptions"
									:key="app_item.value"
									:label="app_item.label"
									:value="app_item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="路径" label-width="46px" style="margin-top: 15px">
							<el-input style="width: 90%;" v-model="item.miniprogram.page"></el-input>
						</el-form-item>
					</el-form-item>
					<el-form-item v-if="'link' === item.msgtype" style="margin-top: 22px">
						<el-form-item label="标题" label-width="46px" style="margin-top: 15px" :rules="[{ max: 128, message: '长度在 128 个字符以内', trigger: 'blur' }]">
							<el-input style="width: 90%;" v-model="item.link.title"></el-input>
						</el-form-item>
						<el-form-item label="封面" label-width="46px" style="margin-top: 15px">
							<el-upload class = "avatar-uploader" v-model = "item.link.picurl"
									   :action="$http.adornUrl('alioss/upload')"  :show-file-list = "false"
									   :on-success = "res => handleLinkSuccess(res, item)"
									   style = "width:148px; height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;"
							>
								<img v-if = "item.link.picurl" :src = "item.link.picurl" class = "avatar"
									 style = "border-radius: 6px;width: 148px;height: 148px;"/>
								<i v-else class = "el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</el-form-item>
						<el-form-item label="描述" label-width="46px" style="margin-top: 15px" :rules="[{ max: 512, message: '长度在 512 个字符以内', trigger: 'blur' }]">
							<el-input type="textarea" :autosize="{ minRows: 3, maxRows: 8}"  style="width: 90%;" v-model="item.link.desc"></el-input>
						</el-form-item>
						<el-form-item label="url" label-width="46px" style="margin-top: 15px" :rules="[{ max: 1024, message: '长度在 1024 个字符以内', trigger: 'blur' }]">
							<el-input style="width: 90%;" v-model="item.link.url"></el-input>
						</el-form-item>
					</el-form-item>
					<el-form-item v-if="'image' === item.msgtype" style="margin-top: 22px" :rules="[{ required: true, message: '请上传图片', trigger: 'blur' }]">
						<el-upload class = "avatar-uploader"
								   :show-file-list = "false"
								   :action="$http.adornUrl('enterpriseWechat/tempMediaUpload/image')"
								   :on-success="res => handleImageSuccess(res, item, idx)"
								   :before-upload="beforeImageUpload"
								   :multiple="false"
								   accept="image/*"
								   style = "width:148px; height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;"
						>
							<img v-if="item.image.mediaId" :src = "$http.adornUrl('enterpriseWechat/getTempMedia/'+item.image.mediaId)"
								 style = "border-radius: 6px;width: 148px;height: 148px;"/>
							<i v-else class = "el-icon-plus avatar-uploader-icon" style = "width: 148px;height: 148px;"></i>
						</el-upload>
					</el-form-item>
					<el-form-item v-if="'video' === item.msgtype" style="margin-top: 22px" :rules="[{ required: true, message: '请上传视频', trigger: 'blur' }]">
						<el-upload class="avatar-uploader" v-model="item.video.mediaId"
								   :action="$http.adornUrl('enterpriseWechat/tempMediaUpload/video')"
								   :show-file-list="false"
								   :on-success="res => handleVideoSuccess(res, item, idx)"
								   :before-upload="beforeVideoUpload"
								   :multiple="false"
								   accept=".mp4,.MP4"
								   style = "width:148px; height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;"
						>
							<video v-if="item.video.mediaId" :src="$http.adornUrl('enterpriseWechat/getTempMedia/'+item.video.mediaId)" class="avatar" controls="controls"
								   style="width: 148px;height:148px;">您的浏览器不支持视频播放</video>
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
						</el-upload>
					</el-form-item>
					<el-form-item v-if="'file' === item.msgtype" style="margin-top: 22px" :rules="[{ required: true, message: '请上传文件', trigger: 'blur' }]">
						<el-upload
							:action="$http.adornUrl('enterpriseWechat/tempMediaUpload/file')"
							:on-success="res => handleFileSuccess(res, item, idx)"
							:before-upload="beforeFileUpload"
							:multiple="false"
							:auto-upload="false"
							:limit="1"
						>
							<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
						</el-upload>
					</el-form-item>
				</el-form-item>
				<el-form-item  style="text-align: center">
					<el-button type = "primary" @click = "submitAddMsg()">立即创建</el-button>
					<el-button style="margin-left: 20px !important;" @click = "addMsgDialogVisible = false">取 消</el-button>
				</el-form-item>
			</el-form>
		</el-dialog>
	</div>
</template>

<script>
export default {
	name: "enter-wechat",
	data() {
		return {
			limit: 20,
			cursor: "",
			tableData: [],
			addMsgDialogVisible: false,
			addMsgForm: {
				text: {},
				tagFilter:{},
				sendType: "label",
				chatType: "single",
			},
			tagGroup: [],
			tableDataLoading: false,
			selectedTag:[],
			dynamicAttachment:[],
			imageUrl: "",
			appOptions: [
				// {
				// 	label : "小程序1",
				// 	value: "__WX_APPID_CANDLE__"
				// },
				// {
				// 	label : "小程序2",
				// 	value: "__WX_APPID_HANCHAN__"
				// },
				// {
				// 	label : "小程序3",
				// 	value: "__WX_APPID_QINGFANG__"
				// },
				// {
				// 	label : "小程序4",
				// 	value: "__WX_APPID_NINGQI__"
				// },
				// {
				// 	label : "小程序5",
				// 	value: "__WX_APPID_YUHAO__"
				// },
				{
					label : "小程序6",
					value: "__WX_APPID_RANK__"
				},
			],
		}
	},
	methods: {
		addAttachment(){
			if (this.dynamicAttachment.length === 9) {
				this.$message.error("最多添加 9 个附件！");
				return false;
			}
			this.dynamicAttachment.push({
				msgtype: "miniprogram",
				image:{
					mediaId:"",
				},
				link:{
					picurl: "",
				},
				miniprogram:{
					picMediaId: ""
				},
				video:{
					mediaId:"",
				},
				file:{
					mediaId:"",
				},
			});
		},
		removeAttachment(idx){
			this.$confirm(`确定删除此条附件?`, '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.dynamicAttachment.splice(idx, 1)
			});
		},


		handleMiniSuccess(file, item){
			item.miniprogram.picMediaId = file.data.mediaId
		},
		beforeMiniUpload(file){
			const isLt10M = file.size / 1024 / 1024 < 10;
			const isGt5K = file.size > 5;
			if (!isLt10M) {
				this.$message.error('上传图片大小不能超过 10MB!');
			}
			if (!isGt5K) {
				this.$message.error('上传图片大小必须大于 5KB!');
			}
			return isLt10M && isGt5K;
		},

		handleLinkSuccess(file, item){
			item.link.picurl = file.data
		},

		handleImageSuccess(file, item){
			item.image.mediaId = file.data.mediaId
		},
		beforeImageUpload(file){
			const isLt10M = file.size / 1024 / 1024 < 10;
			const isGt5K = file.size > 5;
			if (!isLt10M) {
				this.$message.error('上传图片大小不能超过 10MB!');
			}
			if (!isGt5K) {
				this.$message.error('上传图片大小必须大于 5KB!');
			}
			return isLt10M && isGt5K;
		},

		handleVideoSuccess(file, item){
			item.video.mediaId = file.data.mediaId
		},
		beforeVideoUpload(file){
			const isLt10M = file.size / 1024 / 1024 < 10;
			const isGt5K = file.size > 5;
			if (!isLt10M) {
				this.$message.error('上传视频大小不能超过 10MB!');
			}
			if (!isGt5K) {
				this.$message.error('上传视频大小必须大于 5KB!');
			}
			return isLt10M && isGt5K;
		},

		handleFileSuccess(file, item){
			item.file.mediaId = file.data.mediaId
		},
		beforeFileUpload(file){
			const isLt20M = file.size / 1024 / 1024 < 20;
			const isGt5K = file.size > 5;
			if (!isLt20M) {
				this.$message.error('上传文件大小不能超过 20MB!');
			}
			if (!isGt5K) {
				this.$message.error('上传文件大小必须大于 5KB!');
			}
			return isLt20M && isGt5K;
		},

		showAddMsgDialog() {
			this.addMsgDialogVisible = true
			this.labelData()
		},


		submitAddMsg() {
			this.$refs['form'].validate((valid) => {
				if (valid) {
					if ('all' !== this.sendType) {
						this.addMsgForm.tagFilter.groupList = [];
						const groups = {};
						this.selectedTag.forEach(o => {
							const strs = o.split(":")
							groups[strs[0]] = groups[strs[0]] || [];
							groups[strs[0]].push(strs[1]);
						});
						Object.values(groups).forEach((item, idx) => {
							this.addMsgForm.tagFilter.groupList.push({"tagList": item})
						})
					}

					this.addMsgForm.attachments = this.dynamicAttachment.map(item => {
						let res = {
							"msgtype": item.msgtype,
						}
						res[item.msgtype] = item[item.msgtype]
						return res;
					})

					this.$http({
						url: this.$http.adornUrl('enterpriseWechat/addMsgTemplate'),
						method: 'post',
						data: this.$http.adornData(this.addMsgForm),
					}).then(() => {
						this.addMsgForm = {text: {}, sendType: "label", chatType: "single", tagFilter: {},}
						this.dataSelect()
					})
					this.addMsgDialogVisible = false
				}
			});
		},
		loadMore() {
			if (!this.cursor) {
				return false;
			}
			// 分页
			this.dataSelect()
		},
		// 获取数据列表
		dataSelect() {
			this.tableDataLoading = true
			this.$http({
				url: this.$http.adornUrl('enterpriseWechat/getGroupMsgListV2'),
				method: 'post',
				data: this.$http.adornData({
					limit: this.limit,
					cursor: this.cursor,
				}),
			}).then(({
						 data
					 }) => {
				this.tableDataLoading = false
				if (this.tableData.groupMsgList) {
					this.tableData.groupMsgList.push(...data.data.groupMsgList);
				} else {
					this.tableData = data.data;
				}
				this.cursor = data.data.next_cursor
			})
		},
		// 获取数据列表
		labelData() {
			this.$http({
				url: this.$http.adornUrl('enterpriseWechat/getCorpTagList'),
				method: 'post',
				data: this.$http.adornData(),
			}).then(({
						 data
					 }) => {
				this.tagGroup = data.data.tagGroup.sort((a, b) => {
					return b.order - a.order
				})
			})
		},
	},
	mounted() {
		this.dataSelect();
		this.labelData();
	}
};

</script>

<style scoped>
	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		text-align: center;
	}
</style>
