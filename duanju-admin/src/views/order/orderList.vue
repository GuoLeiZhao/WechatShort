<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="订单列表" name="first">
			<div style="margin-right:2%;">
				<span>起止日期：</span>
				<el-date-picker
					v-model="dateList"
					type="datetimerange"
					range-separator="至"
					start-placeholder="开始日期"
					end-placeholder="结束日期"
					format="yyyy-MM-DD HH:mm:ss"
					value-format="yyyy-MM-DD HH:mm:ss"
					@change="dateChangeSelect"
				>
				</el-date-picker>&nbsp;&nbsp;&nbsp;&nbsp;
				<div style="position: relative;display: inline-block;">
					<span>商品名称：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入商品名称"
							  v-model="nameLike"></el-input>&nbsp;&nbsp;
				</div>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
				</el-button>
				<!--			  <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="createOrder">新建订单-->
				<!--			  </el-button>-->
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData">
				<el-table-column fixed prop="id" label="订单ID"></el-table-column>
				<el-table-column prop="itemId" label="商品ID"></el-table-column>
				<el-table-column prop="itemName" label="商品名称"></el-table-column>
				<el-table-column prop="orderStatus" label="订单状态">
					<template slot-scope="scope">
						<span v-if="scope.row.orderStatus == 'WAITING'">待付款</span>
						<span v-if="scope.row.orderStatus == 'PAID'">已付款</span>
						<span v-if="scope.row.orderStatus == 'CANCELED'">已取消</span>
						<span v-if="scope.row.orderStatus == 'FINISHED'">已完成</span>
					</template>
				</el-table-column>
				<el-table-column prop="buyerName" label="收件人姓名">
				</el-table-column>
				<el-table-column prop="buyerPhone" label="订单电话">
				</el-table-column>
				<el-table-column prop="buyerAddress" label="收件人地址"></el-table-column>
				<el-table-column prop="transferNo" label="物流编号">
				</el-table-column>
				<el-table-column prop="deliveryStatus" label="物流状态">
					<template slot-scope="scope">
						<span v-if="scope.row.deliveryStatus == 'NOT_DELIVERED'">未发货</span>
						<span v-if="scope.row.deliveryStatus == 'PICKED_UP'">已揽收</span>
						<span v-if="scope.row.deliveryStatus == 'DELIVERED'">已发货</span>
					</template>
				</el-table-column>
				<el-table-column prop="remark" label="备注"></el-table-column>
				<el-table-column prop="deliveryDate" label="发货时间"></el-table-column>
				<el-table-column prop="createdAt" label="创建时间"></el-table-column>
				<el-table-column label="操作" align="center" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" style="margin:5px;"
								   @click="editOrder(scope.row.id)">编辑
						</el-button>
						<el-button size="mini" type="danger" style="margin:5px;"
								   @click="deletes(scope.row.id)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;float:right">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							   :page-sizes="[10, 20, 30, 40, 100, 200, 500]"
							   :page-size="limit" :current-page="page" layout="total,sizes, prev, pager, next,jumper"
							   :total="tableData.totalCount">
				</el-pagination>
			</div>
			<el-dialog title="创建订单" :visible.sync="showCreateOrderDialog" center width="50%">
				<el-form :model="createOrderDetail" :rules="createOrderRules" ref="createOrderForm" label-width="100px">
					<el-form-item label="商品名称" prop="item_id">
						<el-select v-model="createOrderDetail.itemId" filterable placeholder="请选择商品">
							<el-option
								v-for="item in goodsList"
								:key="item.id"
								:label="item.name"
								:value="item.id">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="订单状态" prop="orderStatus">
						<el-select v-model="createOrderDetail.orderStatus" filterable placeholder="请选择订单状态">
							<el-option
								v-for="item in orderStatusList"
								:key="item.value"
								:label="item.label"
								:value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="物流状态" prop="deliveryStatus">
						<el-select v-model="createOrderDetail.deliveryStatus" filterable placeholder="请选择订单状态">
							<el-option
								v-for="item in deliveryStatusList"
								:key="item.value"
								:label="item.label"
								:value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="备注" prop="remark">
						<el-input
							type="textarea"
							:rows="4"
							style="width: 300px"
							placeholder="请输入备注"
							v-model="createOrderDetail.remark">
						</el-input>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="closeCreateDialog">取 消</el-button>
					<el-button type="primary" @click="createOrders()">确 定</el-button>
				</div>
			</el-dialog>
			<el-dialog title="编辑订单" :visible.sync="showEditOrderDialog" center width="50%">
				<el-form :model="editOrderDetail" ref="editOrderForm" label-width="100px">
					<el-form-item label="订单编号" prop="id">
						<span>{{ editOrderDetail.id }}</span>
					</el-form-item>
					<el-form-item label="商品ID" prop="itemId">
						<span>{{ editOrderDetail.itemId }}</span>
					</el-form-item>
					<el-form-item label="商品名称" prop="itemName">
						<span>{{ editOrderDetail.itemName }}</span>
					</el-form-item>
					<el-form-item label="订单状态" prop="orderStatus">
						<el-select v-model="editOrderDetail.orderStatus" filterable placeholder="请选择订单状态">
							<el-option
								v-for="item in orderStatusList"
								:key="item.value"
								:label="item.label"
								:value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="物流状态" prop="deliveryStatus">
						<el-select v-model="editOrderDetail.deliveryStatus" filterable placeholder="请选择订单状态">
							<el-option
								v-for="item in deliveryStatusList"
								:key="item.value"
								:label="item.label"
								:value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="物流编号" prop="transferNo">

						<el-input
							style="width: 300px"
							placeholder="请输入物流编号"
							v-model="editOrderDetail.transferNo">
						</el-input>
					</el-form-item>
					<el-form-item label="发货日期" prop="deliveryDate">
						<el-date-picker
							v-model="editOrderDetail.deliveryDate"
							type="date"
							value-format="yyyy-MM-dd"
							placeholder="选择发货日期">
						</el-date-picker>
					</el-form-item>
					<el-form-item label="备注" prop="remark">
						<el-input
							type="textarea"
							:rows="4"
							style="width: 300px"
							placeholder="请输入备注"
							v-model="editOrderDetail.remark">
						</el-input>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="closeEditDialog">取 消</el-button>
					<el-button type="primary" @click="editOrders()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane>
	</el-tabs>
</template>
<script>
import {clearData} from '../../utils';

export default {
	data() {
		return {
			orderStatusList: [
				{label: '待付款', value: 'WATTING'},
				{label: '已付款', value: 'PAID'},
				{label: '已取消', value: 'CANCELED'},
				{label: '已完成', value: 'FINISHED'},
			],
			deliveryStatusList: [
				{label: '未发货', value: 'NOT_DELIVERED',},
				{label: '已揽收', value: 'PICKED_UP',},
				{label: '已发货', value: 'DELIVERED',},
			],
			dateList: [],
			page: 1,
			limit: 10,
			activeName: 'first',
			tableDataLoading: true,
			tableData: [],
			checkBoxData: [],//多选框选择的值
			dataList: [], // 起止日期
			createdStart: '',
			createdEnd: '',
			nameLike: '',
			showCreateOrderDialog: false,
			showEditOrderDialog: false,
			goodsList: [], // 全量商品
			createOrderDetail: {
				itemId: '',
				orderStatus: 'WAITING',
				deliveryStatus: 'NOT_DELIVERED',
				remark: '',
			},
			editOrderDetail: {
				id: '',
				itemId: '',
				itemName: '',
				orderStatus: '',
				deliveryStatus: '',
				transferNo: '',
				deliveryDate: '',
				remark: '',
			},
			createOrderRules: {
				itemId: [
					{required: true, message: '请选择商品', trigger: 'change'}
				],
			}
		}
	},
	methods: {
		createOrder() {
			this.$http({
				url: this.$http.adornUrl('shop/item/selectPage'),
				method: 'get',
				params: this.$http.adornParams({
					'currPage': 1,
					'pageSize': 1000,
					// 'shelves': true,
				})
			}).then(({data}) => {
				if (data && data.code === 0) {
					this.goodsList = data.data.records;
					this.showCreateOrderDialog = true;
				} else {
					this.$message.error(data.msg);
				}
			})
		},
		closeCreateDialog() {
			this.showCreateOrderDialog = false;
			this.createOrderDetail = {
				itemId: '',
				orderStatus: 'WAITING',
				deliveryStatus: 'NOT_DELIVERED',
				remark: '',
			};
			this.$refs['createOrderForm'].resetFields();
		},
		createOrders() {
			this.$refs['createOrderForm'].validate((valid) => {
				if (valid) {
					this.$http({
						url: this.$http.adornUrl('shop/order'),
						method: 'post',
						data: this.$http.adornData({...this.createOrderDetail})
					}).then(({data}) => {
						if (data && data.code === 0) {
							// 成功
							this.$message({
								message: '添加成功',
								type: 'success',
								duration: 1000,
								onClose: () => {
									this.closeCreateDialog();
									this.dataSelect()
								}
							})
						}
					})
				} else {
					this.$message.error('请校验填写内容');
				}
			})
		},
		editOrder(id) {
			this.$http({
				url: this.$http.adornUrl(`shop/order/selectById/${id}`),
				method: 'get',
			}).then(({data}) => {
				if (data && data.code === 0) {
					const {id, itemId, itemName, orderStatus, deliveryStatus, deliveryDate, transferNo, remark} = data.data;
					this.editOrderDetail = {
						id, itemId, itemName, orderStatus, deliveryStatus, deliveryDate, transferNo,  remark
					};
					this.showEditOrderDialog = true;
				} else {
					this.$message.error(data.msg);
				}
			})
		},
		closeEditDialog() {
			this.showEditOrderDialog = false;
			this.editOrderDetail = {
				id: '',
				itemId: '',
				itemName: '',
				orderStatus: '',
				deliveryStatus: '',
				transferNo: '',
				deliveryDate: '',
				remark: '',
			},
				this.$refs['editOrderForm'].resetFields();
		},
		editOrders() {
			this.$refs['editOrderForm'].validate((valid) => {
				if (valid) {
					this.$http({
						url: this.$http.adornUrl('/shop/order'),
						method: 'put',
						data: this.$http.adornData({...this.editOrderDetail})
					}).then(({data}) => {
						if (data && data.code === 0) {
							// 成功
							this.$message({
								message: '修改成功',
								type: 'success',
								duration: 1000,
								onClose: () => {
									this.closeEditDialog()
									this.dataSelect()
								}
							})
						} else {
							this.$message.error(data.msg)
						}
					})
				} else {
					this.$message.error('请校验信息')
				}
			})
		},
		// 日期选择时的查询
		dateChangeSelect() {
			console.log(this.dateList, '_val');
			if (this.dateList && this.dateList.length > 0) {
				this.createdStart = this.dateList[0];
				this.createdEnd = this.dateList[1];
			} else {
				this.createdStart = '';
				this.createdEnd = '';
			}
		},
		// tabs切换
		handleClick(tab, event) {
			if (tab._props.label == '订单列表') {
				this.page = 1
				this.limit = 10
				this.classify = 1
				this.dataSelect()
			}
		},
		handleSizeChange(val) {
			this.limit = val
			this.dataSelect()
		},
		handleCurrentChange(val) {
			this.page = val
			this.dataSelect()
		},
		// 查询资源列表
		select() {
			this.page = 1
			this.limit = 10
			this.dataSelect()
		},
		// 重置资源列表
		cleans() {
			this.ordersNo = ''
			this.status = ''
			this.page = 1
			this.dataSelect()
		},
		// select选择事件
		animeDat(state) {
			this.page = 1
			this.status = state
			console.log(state)
			this.dataSelect()
		},
		// 删除banner图
		deletes(id) {
			this.$confirm(`确定删除此条信息?`, '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$http({
					url: this.$http.adornUrl(`/shop/order/${id}`),
					method: 'get',
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
		// 获取数据列表
		dataSelect() {
			this.tableDataLoading = true
			this.totalMoney = 0
			this.$http({
				url: this.$http.adornUrl('shop/order/selectPage'),
				method: 'get',
				params: this.$http.adornParams(clearData({
					'currPage': this.page,
					'pageSize': this.limit,
					'createStart': this.createStart,
					'createEnd': this.createEnd,
					'nameLike': this.nameLike,
				}))
			}).then(({data}) => {
				if (data && data.code === 0) {
					this.tableDataLoading = false
					let returnData = data.data.records
					this.tableData = returnData
				}
			})
		},
		// 详情跳转
		updates(row) {
			this.$router.push({path: '/userDetail', query: {userId: row.userId}})
		},
		// 退款
		tuikuanBtn(row) {
			let delid = row.ordersId
			this.$confirm(`确定退款吗?`, '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$http({
					url: this.$http.adornUrl('order/refundOrders?ordersId=' + delid),
					method: 'post',
					params: this.$http.adornParams({})
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
							onClose: () => {
							}
						})
					}

				})
			})
		},
	},
	mounted() {
		this.dataSelect()
	}
}
</script>

<style>
.customWidth {
	width: 80% !important;
}
</style>
