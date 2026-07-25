<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="注销信息" name="first">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称" width="150">
					<template slot-scope="scope">
						<span style="color: #4f9dec;cursor: pointer;"
							@click="updataDetails(scope.row,scope.row.userEntity)">
							{{ scope.row.userEntity ? scope.row.userEntity.userName : '未绑定' }}
						</span>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="内容">
					<!-- <template slot-scope="scope">
						<span>注销账户</span>
					</template> -->
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('messageZx:shenhe')"
							v-if="scope.row.userEntity!=null" @click="deleteuser(scope.row)">注销用户</el-button>
						<span v-else>用户已注销</span>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-dialog title="审核处理" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
				<el-radio-group v-model="radio">
					<el-radio :label="0">通过</el-radio>
					<el-radio :label="1">驳回</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;" v-if="radio===1">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">驳回理由：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入驳回理由">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;" v-if="radio===0">
				<span style="width: 200px;display: inline-block;text-align: right;">处理结果：</span>
				<el-radio-group v-model="clStatus">
					<el-radio :label="0">封号</el-radio>
					<el-radio :label="1">下架</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
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
				state: 3,
				content: '',
				
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				radio: '',
				clStatus: '',
				messageId: '',
				platform: '',
				platform1: '',
				
			}
		},
		methods: {
			// 详情跳转
			updataDetails(row, userEntity) {
				if (userEntity != null) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: row.userId
						}
					});
				} else {
					this.$message({
						message: '当前用户已注销',
						type: 'warning',
						duration: 1500,
						onClose: () => {}
					})
				}

			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			
			handleClick(tab, event) {
				if (tab._props.label == '注销信息') {
					this.page = 1
					this.limit = 10
					this.state = 3
					this.dataSelect()
				}
			},
			
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`message/selectMessageByType`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.state,
						'platform': this.platform1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					// for (var i in data.data.list) {
					// 	var a = data.data.list[i].content.toString()
					// 	var b = JSON.parse(a)
					// 	data.data.list[i].content = b.content
					// 	console.log('key', b.content)
					// }
					let returnData = data.data;
					this.tableData = returnData

				})
			},
			// 注销
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定要注销该用户?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/deleteUserByUserId/${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
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
				})
			},
			
			//审核弹框
			refuseClick(rows) {
				this.radio = ''
				this.clStatus = ''
				this.content = ''
				this.messageId = rows.id
				this.platform = rows.platform
				this.dialogFormVisible = true
			},
			//提交审核
			refuseto() {
				if (this.radio === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择审核结果',
						type: 'warning'
					});
					return
				}
				if (this.radio === 1) {
					if (this.content == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入拒绝理由',
							type: 'warning'
						});
						return
					}
				}
				if (this.radio === 0) {
					if (this.clStatus === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择处理结果',
							type: 'warning'
						});
						return
					}
				}
				var status = ''
				if (this.radio === 1) {
					status = 1
				} else {
					if (this.clStatus === 0) {
						status = 2
					}
					if (this.clStatus === 1) {
						status = 3
					}
				}
			
				this.$http({
					url: this.$http.adornUrl('message/auditMessage'),
					method: 'post',
					params: this.$http.adornParams({
						'messageId': this.messageId,
						'status': status,
						'auditContent': this.content
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
								this.content = ''
								this.status = -1
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
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style>

</style>