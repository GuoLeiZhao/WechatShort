<template>
	<div>
		<div class="record_date">
			<el-row>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">总用户数</div>
							<div class="text_color"><span>{{ tableData.totalUsers ? tableData.totalUsers : 0 }}</span>人
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">今日新增</div>
							<div class="text_color"><span>{{ tableData.newToday ? tableData.newToday : 0 }}</span>人
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本月新增</div>
							<div class="text_color"><span>{{ tableData.newMonth ? tableData.newMonth : 0 }}</span>人
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本年新增</div>
							<div class="text_color"><span>{{ tableData.newYear ? tableData.newYear : 0 }}</span>人</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">总收入</div>
							<div class="text_color">
								<span>{{ tableData.totalRevenue ? tableData.totalRevenue : 0 }}</span>元
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">今日收入</div>
							<div class="text_color">
								<span>{{ tableData.todayRevenue ? tableData.todayRevenue : 0 }}</span>元
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本月收入</div>
							<div class="text_color">
								<span>{{ tableData.monthRevenue ? tableData.monthRevenue : 0 }}</span>元
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本年收入</div>
							<div class="text_color"><span>{{ tableData.yearRevenue ? tableData.yearRevenue : 0 }}</span>元
							</div>
						</div>
					</div>
				</el-col>

			</el-row>
			<div>
				<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="orderfenxi">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info.stockDate" align="right"
								type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
								@change="animeOrder">
				</el-date-picker>
			</div>
			<el-tabs v-model="activeName" @tab-click="handleClick" style="margin-left: 10px;">
				<el-tab-pane label="用户分析" name="first">
					<el-row>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">总人数</div>
									<div class="text_color">
										<span>{{ taskStat.sumUserCount ? taskStat.sumUserCount : 0 }}</span>人
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">普通用户</div>
									<div class="text_color">
										<span>{{ taskStat.userCount ? taskStat.userCount : 0 }}</span>人
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">会员人数</div>
									<div class="text_color">
										<span>{{ taskStat.memberCount ? taskStat.memberCount : 0 }}</span>人
									</div>
								</div>
							</div>
						</el-col>
						<!-- <el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">手机号用户</div>
									<div class="text_color">
										<span>{{taskStat.phoneUserCount ? taskStat.phoneUserCount : 0}}</span>人
									</div>
								</div>
							</div>
						</el-col> -->
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">APP用户</div>
									<div class="text_color">
										<span>{{ taskStat.appCount ? taskStat.appCount : 0 }}</span>人
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">H5用户</div>
									<div class="text_color">
										<span>{{ taskStat.h5Count ? taskStat.h5Count : 0 }}</span>人
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">小程序用户</div>
									<div class="text_color">
										<span>{{ taskStat.wxCount ? taskStat.wxCount : 0 }}</span>人
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">当前在线人数</div>
									<div class="text_color"><span>{{ taskStat2 ? taskStat2 : 0 }}</span>人</div>
								</div>
							</div>
						</el-col>

					</el-row>
				</el-tab-pane>
				<el-tab-pane label="资源收入分析" name="fourth">
					<el-table v-loading="tableDataLoading" :data="tableData2.list">
						<el-table-column prop="" label="编号" width="100">
							<template slot-scope="scope">
								<span>{{ scope.$index + 1 }}</span>
							</template>
						</el-table-column>
						<el-table-column prop="coursename" label="资源名称">
						</el-table-column>
						<el-table-column prop="coursemoney" label="平台收入">
						</el-table-column>

					</el-table>
					<div style="text-align: center;margin-top: 10px;float:right">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
									   :page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
									   layout="total,sizes, prev, pager, next,jumper" :total="tableData2.totalCount">
						</el-pagination>
					</div>
				</el-tab-pane>
				<el-tab-pane label="订单统计" name="second">
					<el-row>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">总订单</div>
									<div class="text_color">
										<span>{{ orderStat.sumCourseOrdersCount ? orderStat.sumCourseOrdersCount : 0 }}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待支付订单</div>
									<div class="text_color">
										<span>{{ orderStat.daiCourseKeOrdersCount ? orderStat.daiCourseKeOrdersCount : 0 }}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已完成订单</div>
									<div class="text_color">
										<span>{{ orderStat.wanCourseKeOrdersCount ? orderStat.wanCourseKeOrdersCount : 0 }}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">退款订单</div>
									<div class="text_color">
										<span>{{ orderStat.tuiCourseOrdersCount ? orderStat.tuiCourseOrdersCount : 0 }}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">课程总金额</div>
									<div class="text_color">
										<span>{{ orderStat.sumCourseOrdersMoney ? orderStat.sumCourseOrdersMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">待支付金额</div>
									<div class="text_color">
										<span>{{ orderStat.daiCourseOrdersMoney ? orderStat.daiCourseOrdersMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">已支付金额</div>
									<div class="text_color">
										<span>{{ orderStat.wanCourseOrdersMoney ? orderStat.wanCourseOrdersMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">退款金额</div>
									<div class="text_color">
										<span>{{ orderStat.tuiCourseOrdersMoney ? orderStat.tuiCourseOrdersMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">会员总订单</div>
									<div class="text_color">
										<span>{{ orderStat.sumMemberOrdersCount ? orderStat.sumMemberOrdersCount : 0 }}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">会员待支付订单</div>
									<div class="text_color">
										<span>{{ orderStat.daiMemberKeOrdersCount ? orderStat.daiMemberKeOrdersCount : 0 }}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">会员已完成订单</div>
									<div class="text_color">
										<span>{{ orderStat.wanMemberKeOrdersCount ? orderStat.wanMemberKeOrdersCount : 0 }}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">会员退款订单</div>
									<div class="text_color">
										<span>{{ orderStat.tuiMemberOrdersCount ? orderStat.tuiMemberOrdersCount : 0 }}</span>单
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">会员课程总金额</div>
									<div class="text_color">
										<span>{{ orderStat.sumMemberOrdersMoney ? orderStat.sumMemberOrdersMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">会员待支付金额</div>
									<div class="text_color">
										<span>{{ orderStat.daiMemberOrdersMoney ? orderStat.daiMemberOrdersMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">会员已支付金额</div>
									<div class="text_color">
										<span>{{ orderStat.wanMemberOrdersMoney ? orderStat.wanMemberOrdersMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">会员退款金额</div>
									<div class="text_color">
										<span>{{ orderStat.tuiMemberOrdersMoney ? orderStat.tuiMemberOrdersMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
					</el-row>
				</el-tab-pane>
<!--				<el-tab-pane label="充值统计" name="sixth">
					<el-row>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">充值总金额</div>
									<div class="text_color">
										<span>{{ rechgeData.sumMoney ? rechgeData.sumMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">小程序充值金额</div>
									<div class="text_color">
										<span>{{ rechgeData.weiXinXCXMoney ? rechgeData.weiXinXCXMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">微信公众号充值金额</div>
									<div class="text_color">
										<span>{{ rechgeData.weiXinGZHMoney ? rechgeData.weiXinGZHMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">微信充值金额</div>
									<div class="text_color">
										<span>{{ rechgeData.weiXinMoney ? rechgeData.weiXinMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">支付宝充值金额</div>
									<div class="text_color">
										<span>{{ rechgeData.zhiFuBaoMoney ? rechgeData.zhiFuBaoMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">系统充值</div>
									<div class="text_color">
										<span>{{ rechgeData.xiTongMoney ? rechgeData.xiTongMoney : 0 }}</span>元
									</div>
								</div>
							</div>
						</el-col>

					</el-row>
				</el-tab-pane>
				<el-tab-pane label="看剧统计" name="second">

				</el-tab-pane>-->
			</el-tabs>
		</div>
	</div>
</template>

<script>
export default {
	data() {
		return {
			limit: 10,
			page: 1,
			tableData: [],
			tableData2: [],
			colonelData: [],
			commodityData: [],
			userjfData: [],
			incomeData: [],
			activeName: 'first',
			tableDataLoading: true,
			usersumData: {},
			taskStat: {},
			taskStat2: 0,
			data: '',
			time: '',
			platform: '',
			type: '',
			balance: -1,
			flag: 1,
			info: {
				stockDate: this.getNowTime(), //日期
			},
			flags: [{
				value: 1,
				label: '按天查询'
			}, {
				value: 2,
				label: '按月查询'
			}, {
				value: 3,
				label: '按年查询'
			}],
			orderStat: {},
			rechgeData: {},
		}
	},
	mounted() {
		this.colonel()
		this.taskData()
		this._getData() //用户统计
		// this.incomeSelect() //收入
	},
	methods: {
		// 详情跳转
		updates(row) {
			this.$router.push({
				path: '/userDetail',
				query: {
					userId: row.userId
				}
			})
		},
		updates1(row) {
			this.$router.push({
				path: '/userDetail',
				query: {
					userId: row.userId1
				}
			})
		},
		updates2(row) {
			this.$router.push({
				path: '/userDetail',
				query: {
					userId: row.userId2
				}
			})
		},
		handleSizeChange(val) {
			this.limit = val;
			this.colonel();
		},
		handleCurrentChange(val) {
			this.page = val;
			this.colonel();
		},
		// 用户分析选择日期
		animeDat() {
			this._getData()
		},
		// 订单分析选择日期
		animeOrder() {
			this.taskData()
			this.colonel()
			this.colonelOrder()
		},
		// 订单分析年月日
		orderfenxi(value) {
			let vanumber = value
			if (vanumber === 1) {
				this.flag = 1
				this.taskData()
				this.colonel()
				this.colonelOrder()
			}
			if (vanumber === 2) {
				this.flag = 2
				this.taskData()
				this.colonel()
				this.colonelOrder()
			}
			if (vanumber === 3) {
				this.flag = 3
				this.taskData()
				this.colonel()
				this.colonelOrder()
			}
		},
		// Tabs点击事件
		handleClick(tab, event) {
			if (tab._props.label == '用户分析') {
				this.taskData()
			}
			if (tab._props.label == '资源收入分析') {
				this.colonel()
			}
			if (tab._props.label == '订单统计') {
				this.colonelOrder()
			}
			if (tab._props.label == '充值统计') {
				this.rechSelect()
			}
		},
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
		// 用户统计
		_getData() {
			this.$http({
				url: this.$http.adornUrl('user/homeMessage'),
				method: 'get',
				params: this.$http.adornParams({})
			}).then(({
						 data
					 }) => {
				if (data.code == 0) {
					let returnData = data.data;
					this.tableData = returnData
				}
				console.log('this.tableData', this.tableData)
			})
		},
		// 收入统计
		incomeSelect() {
			this.$http({
				url: this.$http.adornUrl('statistical/income'),
				method: 'get',
				params: this.$http.adornParams({})
			}).then(({
						 data
					 }) => {
				if (data.code == 0) {
					let returnData = data.data;
					this.incomeData = returnData
				}
			})
		},
		//用户分析
		taskData() {
			this.$http({
				url: this.$http.adornUrl('user/userMessage'),
				method: 'get',
				params: this.$http.adornParams({
					'date': this.info.stockDate,
					'type': this.flag,
				})
			}).then(({
						 data
					 }) => {
				let returnData = data.data;
				this.taskStat = returnData
			})
			this.$http({
				url: this.$http.adornUrl('user/selectUserOnLineCount'),
				method: 'get',
				params: this.$http.adornParams({
					// 'date': this.info.stockDate,
					// 'type': this.flag,
				})
			}).then(({
						 data
					 }) => {
				let returnData = data.data;
				this.taskStat2 = returnData
			})

		},
		// 资源收入分析
		colonel() {
			this.tableDataLoading = true
			this.$http({
				url: this.$http.adornUrl('user/courseMessage'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					'date': this.info.stockDate,
					'type': this.flag,
				})
			}).then(({
						 data
					 }) => {
				if (data.code == 0) {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData
				}
			})
		},
		// 订单统计
		colonelOrder() {
			this.tableDataLoading = true
			this.$http({
				url: this.$http.adornUrl('order/selectOrdersCount'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					'time': this.info.stockDate,
					'flag': this.flag,
				})
			}).then(({
						 data
					 }) => {
				this.tableDataLoading = false
				if (data.code == 0) {
					let returnData = data.data
					this.orderStat = returnData
				}
			})
		},
		// 充值统计
		rechSelect() {
			this.$http({
				url: this.$http.adornUrl('cash/payMember'),
				method: 'get',
				params: this.$http.adornParams({
					'time': this.info.stockDate,
					'flag': this.flag
				})
			}).then(({
						 data
					 }) => {
				if (data.code == 0) {
					let returnData = data.data
					this.rechgeData = returnData
				}
			})
		},
	}
}
</script>

<style scoped>
.box {
	padding: 44px;
	border: 1px solid #eee;
	margin: 15px 10px;
}

.box_num {
	font-size: 14px;
	color: #66b1ff;
}

.box_num .box_color {
	color: #333;
	font-size: 14px;
	margin-bottom: 15px;
}

.box_num div span {
	font-size: 20px;
	margin-left: 5px;
}

.text_color {
	color: #4f9dec;
}

.text_color span {
	margin-right: 5px;
}
</style>
