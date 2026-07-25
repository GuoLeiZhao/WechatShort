<template>
	<div>
		<div style="margin:2% 0;display: inline-block;">
			<el-input style="width: 180px;" @keydown.enter.native="select" clearable placeholder="请输入推送活动名称"
				v-model="integralName"></el-input>
		</div>
		<div style="display: inline-block;">
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
			</el-button>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
			</el-button>
		</div>
		<div style="float: right;margin-right:2%;">
			<el-button style='margin: 10px 0;' :disabled="!isAuth('integral:add')" size="mini" type="primary"
				icon="document" @click="addNotice">添加推送活动</el-button>
			<el-button style='margin: 10px 0;' :disabled="!isAuth('integral:delete') || checkBoxData.length <= 0"
				size="mini" type="danger" icon="document" @click="choideletes()">批量删除</el-button>
		</div>
		<el-table v-loading="tableDataLoading" @selection-change="changeFun" :data="tableData.list">
			<el-table-column type="selection">
			</el-table-column>
			<el-table-column prop="sendMoneyId" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="integralName" label="活动名称" width="150">
			</el-table-column>
			<el-table-column prop="describes" label="活动描述" width="200">
			</el-table-column>
			<el-table-column prop="integralNum" label="活动积分">
			</el-table-column>
			<el-table-column prop="money" label="活动金额">
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			<el-table-column fixed="right" label="操作" width="250">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="!isAuth('integral:update')"
						@click="look(scope.row)">详情
					</el-button>
					<el-button size="mini" type="primary" :disabled="!isAuth('integral:update')"
						@click="updates(scope.row)">修改
					</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('integral:delete')"
						@click="deleteuser(scope.row)">删除
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
		<el-dialog title="添加推送活动" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">活动名称：</span>
				<el-input style="width:50%;" v-model="integralName" placeholder="请输入活动名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">活动积分：</span>
				<el-input style="width:50%;" v-model="integralNum" placeholder="请输入活动积分" type="number"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">活动金额：</span>
				<el-input style="width:50%;" v-model="money" placeholder="请输入活动金额" type="number"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -75px;">活动描述：</span>
				<el-input style="width:50%;" v-model="describes" type="textarea" :rows="4" placeholder="请输入活动描述">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改弹框 -->
		<el-dialog title="修改推送活动" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
				<el-form-item label="活动名称：" :label-width="formLabelWidth">
					<el-input v-model="form.integralName" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="活动积分：" :label-width="formLabelWidth">
					<el-input v-model="form.integralNum" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="活动金额：" :label-width="formLabelWidth">
					<el-input v-model="form.money" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="活动描述：" :label-width="formLabelWidth">
					<el-input v-model="form.describes" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				integralName: '',
				describes: '',
				integralNum: '',
				money: '',
				sendMoneyId: '',
				checkBoxData: [], //多选框选择的值
				form: {
					sendMoneyId: '',
					integralName: '',
					describes: '',
					integralNum: '',
					money: '',
					state: '',
					createTime: ''
				},
				formLabelWidth: '200px',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableDataLoading: true,
				tableData: [],
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
			},
			// 详情跳转
			look(row) {
				this.$router.push({
					path: '/integralDetail',
					query: {
						sendMoneyId: row.sendMoneyId
					}
				})
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			// 查询
			select() {
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.integralName = ''
				this.dataSelect()
			},
			//添加推送活动弹框
			addNotice() {
				this.dialogFormVisible = true
			},
			// 添加推送活动
			addNoticeTo() {
				if (this.integralName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入推送活动名称',
						type: 'warning'
					});
					return
				}
				if (this.describes == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入活动描述',
						type: 'warning'
					});
					return
				}
				if (this.integralNum == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入积分',
						type: 'warning'
					});
					return
				}
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金额',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('integral/insertSendMoney'),
					method: 'post',
					data: this.$http.adornData({
						'integralName': this.integralName,
						'describes': this.describes,
						'integralNum': this.integralNum,
						'money': this.money
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
							this.integralName = ''
							this.describes = ''
							this.integralNum = ''
							this.money = ''
							this.dataSelect()
						}
					})
				})
			},
			// 修改推送活动
			updates(rows) {
				console.log(rows)
				this.dialogFormVisible1 = true
				this.form.sendMoneyId = rows.sendMoneyId
				this.form.integralName = rows.integralName
				this.form.describes = rows.describes
				this.form.integralNum = rows.integralNum
				this.form.money = rows.money
				this.form.createTime = rows.createTime
				this.form.state = rows.state
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('integral/updateSendMoney'),
					method: 'post',
					data: this.$http.adornData({
						'sendMoneyId': this.form.sendMoneyId,
						'integralName': this.form.integralName,
						'describes': this.form.describes,
						'integralNum': this.form.integralNum,
						'money': this.form.money,
						'state': this.form.state,
						'createTime': this.form.createTime
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
			// 批量删除任务
			choideletes(sendMoneyId) {
				var ids = sendMoneyId ? [sendMoneyId] : this.checkBoxData.map(item => {
					return item.sendMoneyId
				})
				this.$http({
					url: this.$http.adornUrl(`integral/deleteSendMoney?sendMoneyIds=${ids}`),
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
			},
			// 删除
			deleteuser(row) {
				let delid = row.sendMoneyId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`integral/deleteSendMoney?sendMoneyIds=${delid}`),
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
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})

			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('integral/selectSendMoneyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'integralName': this.integralName,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style>
</style>
