<template>
	<el-tabs v-model="activeName">
		<el-tab-pane label = "赠送记录" name = "first">
			<div style = "margin: 10px">
				<div style="position: relative;display: inline-block;margin: 5px">
					<span>领取用户：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入用户编号"
							  v-model="userId"></el-input>
				</div>
				<div style="position: relative;display: inline-block; margin: 5px">
					<span>领取状态：</span>
					<el-select v-model="status" placeholder="请选择领取状态"
							   style="width: 200px;"
							   @change="select"
					>
						<el-option key='WAITING' label="待领取" value="WAITING"></el-option>
						<el-option key='FINISHED' label="已发放" value="FINISHED"></el-option>
					</el-select>&nbsp;&nbsp;
				</div>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="createDialog">创建
				</el-button>
			</div>
			<el-table v-loading = "tableDataLoading" :data = "tableData.records" style="margin: 15px 10px 10px;">
				<el-table-column fixed prop = "id" label = "记录编号"></el-table-column>
				<el-table-column prop = "title" label = "记录标题"></el-table-column>
				<el-table-column prop = "num" label = "看点数量"></el-table-column>
				<el-table-column prop = "userId" label = "领取用户编号"></el-table-column>
				<el-table-column prop = "userName" label = "领取用户名称"></el-table-column>
				<el-table-column prop = "statusDesc" label = "领取状态"></el-table-column>
				<el-table-column prop = "receiveTime" label = "领取时间"></el-table-column>
				<el-table-column prop = "createdAt" label = "创建时间"></el-table-column>
				<el-table-column label = "操作" align="center" fixed="right">
					<template slot-scope = "scope">
						<el-button size = "mini" @click="editDialog(scope.row.id)">编辑</el-button>
						<el-button size = "mini" type = "danger" @click = "deletes(scope.row.id)" >删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style = "text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change = "handleSizeChange" @current-change = "handleCurrentChange" :page-sizes = "[10, 20, 30, 40, 100, 200, 500]"
							   :page-size = "limit" :current-page = "page" layout = "total,sizes, prev, pager, next,jumper"
							   :total = "tableData.totalCount">
				</el-pagination>
			</div>
			<el-dialog title="创建赠送 URL" :visible.sync="showCreateDialog" center width="40%">
				<el-form :model="createGoodsDetail" :rules="createRules" ref="createForm" label-width="100px">
					<el-form-item label="记录标题" prop="title">
						<el-input v-model="createGoodsDetail.title" style="width: 300px" placeholder="请输入记录标题"></el-input>
					</el-form-item>
					<el-form-item label="赠送数量" prop="num">
						<el-input-number v-model="createGoodsDetail.num" :min="1" label="请输入赠送数量" style="width: 300px"></el-input-number>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="closeCreateDialog">取 消</el-button>
					<el-button type="primary" @click="createGoods()">确 定</el-button>
				</div>

			</el-dialog>
			<el-dialog title="修改赠送 URL" :visible.sync="showEditDislog" center width="80%">
				<el-form :model="editGoodsDetail" :rules="createRules" ref="editForm" label-width="100px">
					<el-form-item label="记录标题" prop="title">
						<el-input v-model="editGoodsDetail.title" style="width: 300px" placeholder="请输入记录标题"></el-input>
					</el-form-item>
					<el-form-item label="赠送数量" prop="num">
						<el-input-number v-model="editGoodsDetail.num" :min="1" label="请输入赠送数量"></el-input-number>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="closeEditDialog">取 消</el-button>
					<el-button type="primary" @click="editGoods()">确 定</el-button>
				</div>

			</el-dialog>
		</el-tab-pane>
	</el-tabs>
</template>
<script>
import { clearData } from '../../utils';
export default {
	name: 'invite',
	data () {
		return {
			activeName: "first",
			userId: "",
			status: "",
			page: 1,
			limit: 10,
			tableDataLoading: true,
			tableData: [],
			dateList: [], // 起止日期
			showCreateDialog: false,
			showEditDislog: false,
			createGoodsDetail: {
				title: '',
				num: 1,
				type: 'POINT'
			},
			editGoodsDetail: {
				id: 0,
				title: '',
				num: 1,
				type: 'POINT'
			},
			createRules: {
				num: [
					{ required: true, message: '请输入赠送数量', trigger: 'blur' },
				]
			},
			editRules: {
				num: [
					{ required: true, message: '请输入赠送数量', trigger: 'blur' },
				]
			},
		}
	},
	methods: {
		// 创建弹窗打开
		createDialog () {
			this.showCreateDialog = true;
		},
		closeCreateDialog() {
			this.showCreateDialog = false;
			this.createGoodsDetail= {
				name: '',
					title: '',
					num: '',
					type: 'POINT'
			};
			this.$refs['createForm'].resetFields();
		},
		// 创建
		createGoods () {
			this.$refs['createForm'].validate((valid) => {
				if (valid) {
					this.$http({
						url: this.$http.adornUrl('activity/invite'),
						method: 'post',
						data: this.$http.adornParams({
							...this.createGoodsDetail
						})
					}).then(({ data }) => {
						if (data && data.code === 0) {
							// 成功
							this.$message({
								message: '添加成功',
								type: 'success',
								duration: 1000,
								onClose: () => {
									this.showCreateDialog = false;
									this.createGoodsDetail= {
										title: '',
										num: '',
										type: 'POINT'
									};
									this.$refs['createForm'].resetFields();
									this.dataSelect()
								}
							})
						} else {
							this.$message.error(data.msg)
						}
					})
				} else {
					this.$message.error('请校验填写内容');
				}
			})
		},
		editDialog(id) {
			console.log(id, 'id');
			this.$http({
				url: this.$http.adornUrl(`activity/invite/selectById/${id}`),
				method: 'get',
			}).then(({ data }) => {
				console.log(data, '_data');
				if (data && data.code === 0) {
					const { id, title, userId, num } = data.data;
					this.editGoodsDetail = {
						id,
						title,
						num,
					};
					this.showEditDislog = true;
				}
			})
		},
		closeEditDialog () {
			this.showEditDislog = false;
			this.editGoodsDetail = {
				id: 0,
				title: '',
				num: '',
				type: 'POINT'
			};
			this.$refs['editForm'].resetFields();
		},
		editGoods() {
			this.$refs['editForm'].validate((valid) => {
				if (valid) {
					this.$http({
						url: this.$http.adornUrl('activity/invite'),
						method: 'put',
						data: this.$http.adornParams({
							...this.editGoodsDetail
						})
					}).then(({ data }) => {
						if (data && data.code === 0) {
							// 成功
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1000,
								onClose: () => {
									this.showEditDislog = false;
									this.editGoodsDetail = {
										title: '',
										num: 0,
									};
									this.$refs['editForm'].resetFields();
									this.dataSelect()
								}
							})
						} else {
							this.$message.error(data.msg)
						}
					})
				} else {
					this.$message.error('请校验填写内容');
				}
			})
		},
		// 封面图片上传
		handleAvatarSuccess5(file) {
			this.createGoodsDetail.pic = file.data;
		},
		// 查询
		handleSizeChange (val) {
			this.limit = val
			this.dataSelect()
		},
		handleCurrentChange (val) {
			this.page = val
			this.dataSelect()
		},
		// 查询资源列表
		select() {
			this.page = 1
			this.limit = 10
			this.dataSelect()
		},
		// 重置列表
		cleans() {
			this.page = 1
			this.status = ''
			this.userId = ''
			this.dataSelect()
		},
		// 删除
		deletes (id) {
			this.$confirm(`确定删除此条信息?`, '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
			}).then(() => {
				this.$http({
					url: this.$http.adornUrl(`activity/invite/${id}`),
					method: 'delete',
				}).then(({data}) => {
					if (data && data.code === 0) {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1000,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg);
					}

				})
			}).catch(() => {
			})
		},
		// 获取数据列表
		dataSelect () {
			this.tableDataLoading = true
			this.totalMoney = 0
			this.$http({
				url: this.$http.adornUrl('activity/invite/selectPage'),
				method: 'get',
				params: this.$http.adornParams(clearData({
					'currPage': this.page,
					'pageSize': this.limit,
					'userId': this.userId,
					'status': this.status,
				}))
			}).then(({data}) => {
				if (data && data.code === 0) {
					this.tableDataLoading = false
					this.tableData = data.data
				}
			})
		},
	},
	mounted () {
		this.dataSelect()
	}
}
</script>

<style>
.customWidth {
	width: 80% !important;
}
</style>
