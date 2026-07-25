<template>
	<el-tabs v-model = "activeName" @tab-click = "handleClick">
		<el-tab-pane label = "订单列表" name = "first">
			<div style = "margin-right:2%;">
				<span>状态：</span>
				<el-select v-model="status" style="width:150px;margin-left: 10px;" @change="animeDat(status)">
					<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
			  <div style="position: relative;display: inline-block;">
			  	<span>订单编号：</span>
			  	<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入订单编号"
			  		v-model="ordersNo"></el-input>&nbsp;&nbsp;
			  </div>
			  <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
			  </el-button>
			  <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
			  </el-button>
			</div>
			<el-table v-loading = "tableDataLoading" :data = "tableData.list" >
			<el-table-column fixed prop = "ordersId" label = "编号" width = "80"></el-table-column>
			<el-table-column prop = "ordersNo" label = "订单编号" width = "180"></el-table-column>
			<el-table-column prop = "userName" label = "购买用户" width = "120" align="center">
				<template slot-scope = "scope">
					<span style = "color: #4f9dec;cursor: pointer;" @click = "updates(scope.row)">
						{{ scope.row.userName}}
					</span>
				</template>
			</el-table-column>
			<el-table-column prop = "title" label = "购买资源/会员等级"  width = "180" align="center">
				<template slot-scope = "scope">
					<span v-if = "scope.row.ordersType == 1">{{scope.row.title}}</span>
					<span v-if = "scope.row.ordersType == 2 && scope.row.vipNameType==0">月卡</span>
					<span v-if = "scope.row.ordersType == 2 && scope.row.vipNameType==1">季卡</span>
					<span v-if = "scope.row.ordersType == 2 && scope.row.vipNameType==2">年卡</span>
				</template>
			</el-table-column>
			<el-table-column prop = "payWay" label = "订单类型"  width = "120">
				<template slot-scope = "scope">
					<span v-if = "scope.row.ordersType == 1">资源</span>
					<span v-if = "scope.row.ordersType == 2">会员</span>
				</template>
			</el-table-column>
			<el-table-column prop = "payWay" label = "支付方式"  width = "120">
				<template slot-scope = "scope">
					<span v-if = "scope.row.payWay == null">暂无</span>
					<span v-if = "scope.row.payWay == 1">微信APP</span>
					<span v-if = "scope.row.payWay == 2">微信公众号</span>
					<span v-if = "scope.row.payWay == 3">微信小程序</span>
					<span v-if = "scope.row.payWay == 4">支付宝</span>
					<span v-if = "scope.row.payWay == 5">会员免费</span>
					<span v-if = "scope.row.payWay == 6">零钱</span>
				</template>
			</el-table-column>
			<el-table-column prop = "payMoney" label = "金额"  width = "120"></el-table-column>
			<el-table-column prop = "status" label = "状态" width="80">
				<template slot-scope = "scope">
					<span v-if = "scope.row.status == null">待支付</span>
					<span v-if = "scope.row.status == 0">待支付</span>
					<span v-if = "scope.row.status == 1">已支付</span>
					<span v-if = "scope.row.status == 2">已退款</span>
				</template>
			</el-table-column>
			<!-- <el-table-column prop = "refundContent" label = "退款原因" ></el-table-column> -->
			<el-table-column prop = "createTime" label = "创建时间" width = "200" ></el-table-column>
			  	<el-table-column label = "操作" width = "80" align="center" fixed="right">
					<template slot-scope = "scope">
						<el-button style="margin:5px;" size="mini" type="warning" icon="document" :disabled="!isAuth('orderCenter:tuikuan')" @click="tuikuanBtn(scope.row)" v-if="scope.row.status==1&&scope.row.payWay != 5">退款
						</el-button>
						<el-button size = "mini" type = "danger" :disabled = "!isAuth('orderCenter:delete')" style="margin:5px;"
								   @click = "deletes(scope.row)">删除</el-button>
					</template>
			  	</el-table-column>
			</el-table>
			<div style="color: #B94A48;font-size: 20px;margin-top: 10px;display: inline-block;">
				本页累计收入统计：{{totalMoney.toFixed(2)}}元； </div>
			<div style = "text-align: center;margin-top: 10px;float:right">
			  	<el-pagination @size-change = "handleSizeChange" @current-change = "handleCurrentChange" :page-sizes = "[10, 20, 30, 40, 100, 200, 500]"
							   :page-size = "limit" :current-page = "page" layout = "total,sizes, prev, pager, next,jumper"
							   :total = "tableData.totalCount">
			  	</el-pagination>
			</div>
		</el-tab-pane>
	</el-tabs>
</template>
<script>
    export default {
		data () {
			return {
				page: 1,
				limit: 10,
				classify: 1,
				ordersNo: '',
				imageUrl: '',
				url: '',
				status: '',
				activeName: 'first',
				tableDataLoading: true,
				tableData: [],
				checkBoxData: [],//多选框选择的值
				statesnum:[
					{
						label: '全部',
						value: ''
					},
					{
						label: '待支付',
						value: 0
					},
					{
						label: '已支付',
						value: 1
					},
					{
						label: '已退款',
						value: 2
					},
				],
				totalMoney: 0,
			}
		},
		methods: {
			// 多选
			changeFun (val) {
				this.checkBoxData = val
			},
			//处理默认选中当前日期
			// getNowTime () {
			// 	var now = new Date()
			// 	var year = now.getFullYear() //得到年份
			// 	var month = now.getMonth() //得到月份
			// 	var date = now.getDate() //得到日期
			// 	var hh = now.getHours() < 10 ? '0' + now.getHours() : now.getHours()
			// 	var mm = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes()
			// 	var ss = now.getSeconds() < 10 ? '0' + now.getSeconds() : now.getSeconds()
			// 	month = month + 1
			// 	month = month.toString().padStart(2, '0')
			// 	date = date.toString().padStart(2, '0')
			// 	var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
			// 	return defaultDate
			// 	this.$set(this.info, 'stockDate', defaultDate)
			// },
			// tabs切换
			handleClick (tab, event) {
				if (tab._props.label == '订单列表') {
					this.page = 1
					this.limit = 10
					this.classify = 1
					this.dataSelect()
				}
			},
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
			deletes (row) {
				let delid = row.ordersId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`order/deleteOrders?ids=${delid}`),
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
			dataSelect () {
				this.tableDataLoading = true
				this.totalMoney = 0
				this.$http({
					url: this.$http.adornUrl('order/selectOrders'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'ordersNo':this.ordersNo,
						'status':this.status
					})
				}).then(({data}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						for (var i in data.data.list) {
							if (data.data.list[i].payMoney) {
								this.totalMoney = this.totalMoney + Number(data.data.list[i].payMoney)
							}
						}
						let returnData = data.data
						this.tableData = returnData
					}
				})
			},
			// 详情跳转
			updates (row) {
				this.$router.push({path: '/userDetail', query: {userId: row.userId}})
			},
			// 退款
			tuikuanBtn(row){
				let delid = row.ordersId
				this.$confirm(`确定退款吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('order/refundOrders?ordersId='+delid),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}else{
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
