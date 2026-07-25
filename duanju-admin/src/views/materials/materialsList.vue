<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="分类管理" name="first">
			<div style="margin: 10px; display: inline-block;">
				<span>分类名称：</span>
				<el-input style="width:150px;" @keydown.enter.native="select" v-model="helpClassifyNameT"
					placeholder="请输入分类名称"></el-input>
			</div>
			<div style="margin:10px;display: inline-block;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
				</el-button>
				<el-button style='margin-left:15px;' :disabled="!isAuth('materialsList:add')" size="mini" type="primary"
					icon="document" @click="addNotice">添加分类</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableDataF.list">
				<el-table-column fixed prop="helpClassifyId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="helpClassifyName" label="分类名称">
				</el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
							@click="updates(scope.$index, scope.row)">编辑
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<!-- 添加弹框 -->
			<el-dialog title="添加分类" :visible.sync="dialogFormVisible" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
					<el-input style="width:50%;" v-model="helpClassifyName" placeholder="请输入分类名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
					<el-input-number size="medium" v-model="sort"></el-input-number>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改弹框 -->
			<el-dialog title="修改分类" :visible.sync="dialogFormVisible1" center>
				<el-form :model="form">
					<el-form-item label="分类名称：" :label-width="formLabelWidth">
						<el-input v-model="form.helpClassifyName" style="width:65%;"></el-input>
					</el-form-item>
				</el-form>
				<el-form :model="form">
					<el-form-item label="排序：" :label-width="formLabelWidth">
						<el-input-number size="medium" v-model="form.sort"></el-input-number>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
		<el-tab-pane label="文章管理" name="second">
			<div style="margin: 10px; display: inline-block;">
				<span>文章分类：</span>
				<el-select v-model="classifyId" placeholder="请选择类型" style="width:150px;"
					@change="articleSelect(classifyId)">
					<el-option v-for="(item,index) in classifyIds" :key="item.index" :label="item.helpClassifyName"
						:value="item.helpClassifyId">
					</el-option>
				</el-select>
			</div>
			<div style="margin: 10px; display: inline-block;">
				<span>文章名称：</span>
				<el-input style="width:150px;" @keydown.enter.native="selectW" v-model="helpWordTitleT"
					placeholder="请输入文章名称"></el-input>
			</div>
			<div style="margin:10px;display: inline-block;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="selectW">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleansW">重置
				</el-button>
				<el-button style='margin-left:15px;' size="mini" :disabled="!isAuth('materialsList:add')" type="primary"
					icon="document" @click="addNoticewz">添加文章</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="helpWordId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="helpWordTitle" label="标题">
					<template slot-scope="scope">
						<div
							style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
							{{scope.row.helpWordTitle}}</div>
					</template>
				</el-table-column>
				<el-table-column prop="helpWordContent" label="内容">
					<template slot-scope="scope">
					 <div
							style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
							{{scope.row.helpWordContent}}</div>
					</template>
				</el-table-column>
				<el-table-column prop="sort" label="排序" width="140">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
							@click="updateswz(scope.$index, scope.row)">编辑
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
							@click="deleteswz(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
			<!-- 添加弹框 -->
			<el-dialog title="添加文章" :visible.sync="dialogFormVisible2" center width="80%">
				<div style="margin-bottom: 10px;">
					<span style="width: 100px;display: inline-block;text-align: right;">文章分类：</span>
					<el-select v-model="classifyIdFl" placeholder="请选择类型" style="width: 80%;">
						<el-option v-for="(item,index) in classifyIds" :key="item.index" :label="item.helpClassifyName"
							:value="item.helpClassifyId">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 100px;display: inline-block;text-align: right;">排序：</span>
					<el-input-number size="medium" v-model="sort"></el-input-number>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 100px;display: inline-block;text-align: right;">文章标题：</span>
					<el-input style="width: 80%;" v-model="helpWordTitle" placeholder="请输入文章标题"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 100px;display: inline-block;text-align: right;">文章内容：</span>

					<quill-editor ref="myTextEditor" v-model="helpWordContent" :options="quillOption"
						style="padding-bottom: 50px;height: 500px;width: 80%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
				</div>

				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="releasNoticeTowz()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改弹框 -->
			<el-dialog title="修改文章" :visible.sync="dialogFormVisible3" center width="80%">
				<el-form :model="form">
					<el-form-item label="文章分类：" :label-width="formLabelWidth">
						<el-select v-model="formwz.helpClassifyId" placeholder="请选择类型" style="width:80%;">
							<el-option v-for="(item,index) in classifyIds" :key="item.index"
								:label="item.helpClassifyName" :value="item.helpClassifyId">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="排序：" :label-width="formLabelWidth">
						<el-input-number size="medium" v-model="formwz.sort"></el-input-number>
					</el-form-item>
					<el-form-item label="文章标题：" :label-width="formLabelWidth">
						<el-input v-model="formwz.helpWordTitle" style="width:80%;"></el-input>
					</el-form-item>
					<el-form-item label="文章内容：" :label-width="formLabelWidth">
						<quill-editor ref="myTextEditor" v-model="formwz.helpWordContent" :options="quillOption"
							style="padding-bottom: 50px;height: 500px;width: 80%;display: inline-table;margin-bottom: 60px;">
						</quill-editor>
					</el-form-item>

				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTowz()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
	</el-tabs>
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
		name: 'news',
		components: {
			quillEditor
		},
		data() {
			return {
				limit: 10,
				page: 1,
				state: 1,
				sort: 0,
				remark: '',
				createTime: '',
				helpClassifyName: '',
				helpClassifyId: '',
				classifyId: 1,
				classifyIds: [],
				form: {
					helpClassifyId: '',
					state: '',
					sort: '',
					helpClassifyName: '',
					createTime: '',
					remark: ''
				},
				helpWordTitle: '',
				helpWordContent: '',
				classifyIdFl: '',
				formwz: {
					helpWordId: '',
					picture: '',
					articleUrl: '',
					helpWordContent: '',
					helpWordTitle: '',
					helpClassifyId: '',
					createTime: '',
					sort: 0,
				},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				picture: '',
				picture2: '',
				articleUrl: '',

				title: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				tableData: {},
				tableDataF: {},
				parentIdnum: '',
				helpClassifyNameT: '',
				classnum: [{
						value: '0',
						label: '一级分类'
					},
					{
						value: '1',
						label: '二级分类'
					}
				],
				value: [],
				quillOption: quillConfig,
				helpWordTitleT: '',
			}
		},
		methods: {
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
			handleClick(tab, event) {
				this.page = 1
				if (tab._props.label == '分类管理') {
					this.dataSelect()
				}
				if (tab._props.label == '文章管理') {
					// this.classifyId = 1
					this.dataSelectW()
					this.articleSelect()
				}
			},
			handleSizeChange(val) {
				this.limit = val;
				this.articleSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.articleSelect()
			},
			handleAvatarSuccess(file) {
				this.picture = file.data;
			},
			handleAvatarSuccess2(file2) {
				this.formwz.picture = file2.data;
			},
			// 添加分类弹框
			addNotice() {
				this.helpClassifyName = ''
				this.sort = 0
				this.dialogFormVisible = true
			},
			// 添加分类
			releasNoticeTo() {
				if (this.helpClassifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择分类名称',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('helpWord/insertHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'state': this.state,
						'sort': this.sort,
						'remark': this.remark,
						'helpClassifyName': this.helpClassifyName,
						'createTime': this.info.stockDate,
						'types':1,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
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
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 编辑分类弹框
			updates(index, rows) {
				this.form.state = rows.state;
				this.form.sort = rows.sort;
				this.dialogFormVisible1 = true;
				this.form.helpClassifyId = rows.helpClassifyId;
				this.form.helpClassifyName = rows.helpClassifyName;
				this.form.remark = rows.remark;
				this.form.createTime = rows.createTime;
			},
			// 修改类别
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl(`helpWord/updateHelpClassify`),
					method: 'post',
					data: this.$http.adornData({
						'helpClassifyId': this.form.helpClassifyId,
						'helpClassifyName': this.form.helpClassifyName,
						'types':1,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
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
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 删除分类
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(
							`helpWord/deleteHelpClassify?helpClassifyId=${row.helpClassifyId}`),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
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
				}).catch(() => {})
			},
			// 添加文章弹框
			addNoticewz() {
				this.helpWordTitle = ''
				this.articleUrl = ''
				this.helpWordContent = ''
				this.picture = ''
				this.sort = 0
				this.dialogFormVisible2 = true
			},
			// 添加文章
			releasNoticeTowz() {
				if (this.classifyIdFl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择文章分类',
						type: 'warning'
					});
					return
				}
				if (this.helpWordTitle == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入文章标题',
						type: 'warning'
					});
					return
				}

				if (this.helpWordContent == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入内容',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('helpWord/insertHelpWord'),
					method: 'post',
					data: this.$http.adornData({
						'helpClassifyId': this.classifyIdFl,
						'helpWordContent': this.helpWordContent,
						'createTime': this.info.stockDate,
						'sort': this.sort,
						'helpWordTitle': this.helpWordTitle,
						
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {

								this.articleSelect()
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
			},
			// 编辑文章弹框
			updateswz(index, rows) {
				this.formwz.picture = rows.picture;
				this.dialogFormVisible3 = true;
				this.formwz.helpWordId = rows.helpWordId;
				this.formwz.helpClassifyId = rows.helpClassifyId
				this.formwz.sort = rows.sort;
				this.formwz.helpWordContent = rows.helpWordContent;
				this.formwz.helpWordTitle = rows.helpWordTitle;
				this.formwz.createTime = rows.createTime;
			},
			// 修改文章
			amendNoticeTowz() {
				this.$http({
					url: this.$http.adornUrl('helpWord/updateHelpWord'),
					method: 'post',
					data: this.$http.adornData({
						'helpWordId': this.formwz.helpWordId,
						'helpWordContent': this.formwz.helpWordContent,
						'helpClassifyId': this.formwz.helpClassifyId,
						'helpWordTitle': this.formwz.helpWordTitle,
						'createTime': this.info.stockDate,
						'sort': this.formwz.sort
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible3 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.articleSelect()
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
			},
			// 删除文章
			deleteswz(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('helpWord/deleteHelpWord'),
						method: 'post',
						params: this.$http.adornParams({
							'helpWordId': row.helpWordId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.name = '';
									this.articleSelect()
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
				}).catch(() => {})
			},
			// 获取分类管理数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpWord/selectHelpClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': 100,
						'helpClassifyName': this.helpClassifyNameT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataF = returnData
				})
			},
			// 分类无分页
			dataSelectW() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpWord/selectHelpClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'helpClassifyName': this.helpClassifyNameT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.classifyId = data.data[0].helpClassifyId
					this.classifyIds = returnData
					this.articleSelect()
				})
			},
			// 获取文章管理数据
			articleSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpWord/selectHelpWordList'),
					method: 'get',
					params: this.$http.adornParams({
						'helpClassifyId': this.classifyId,
						'helpWordTitle': this.helpWordTitleT,
						'page': this.page,
						'limit': this.limit

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			handleChange(value) {
				this.value.forEach(element => {
					this.parentId = element
				})
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.helpClassifyNameT = ''
				this.page = 1
				this.dataSelect()
			},
			// 查询
			selectW() {
				this.page = 1
				this.articleSelect()
			},
			// 重置
			cleansW() {
				this.helpWordTitleT = ''
				this.page = 1
				this.articleSelect()
			},
		},
		mounted() {
			this.dataSelect()
			this.dataSelectW()
		}
	};
</script>

<style>

</style>
