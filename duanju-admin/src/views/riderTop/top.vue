<template>
	<div>
		<!-- <el-tabs v-model="activeName" @tab-click="handleClick"> -->
			<!-- <div style="display: inline-block;" v-if="type != 8">
				<div style="display: inline-block;">
					<span>时间类型：</span>
					<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="orderfenxi">
						<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
					<el-date-picker style="width: 300px;margin-left: 10px;" v-model="info.stockDate" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
						@change="animeOrder">
					</el-date-picker>
				</div>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
				</el-button>
			</div> -->
			<!-- <el-tab-pane label="团长收益排行榜" name="first"> -->
			<div style="text-align: right;">
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">刷新
				</el-button>
			</div>
				
				<el-table v-loading="tableDataLoading" :data="homeData.records">
					<el-table-column fixed prop="user_id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="nick_name" label="代理商昵称">
						<template slot-scope="scope">
							<div style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row)">
								{{scope.row.nick_name?scope.row.nick_name:'未绑定'}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="nickName" label="头像">
						<template slot-scope="scope">
							<div style="color: #4f9dec;cursor: pointer;" v-if="scope.row.avatar">
							<img :src="scope.row.avatar" width="40" height="40" />
							</div>
							<div v-else>暂无头像</div>
						</template>
					</el-table-column>
					<el-table-column prop="money" label="总收益">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="homeData.total">
					</el-pagination>
				</div>
			<!-- </el-tab-pane>
		</el-tabs> -->
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				state: '',
				limit: 10,
				title: '',
				type: 1,
				activeName: 'first',
				tableDataLoading: true,
				homeData: {},
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
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				var hh = now.getHours() < 10 ? '0' + now.getHours() : now.getHours()
				var mm = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes()
				var ss = now.getSeconds() < 10 ? '0' + now.getSeconds() : now.getSeconds()
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			handleSizeChange1(val) {
				this.limit = val
				this.homeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.homeSelect()
			},
			handleClick(tab, event) {
				this.page = 1
				if (tab._props.label == '团长收益排行榜') {
					this.type = 1
					this.homeSelect()
				}
				if (tab._props.label == '会长收益排行榜') {
					this.type = 2
					this.homeSelect()
				}
				if (tab._props.label == '陪玩收益排行榜') {
					this.type = 3
					this.homeSelect()
				}
				if (tab._props.label == '打赏排行版') {
					this.type = 5
					this.homeSelect()
				}
				if (tab._props.label == '消费排行榜') {
					this.type = 6
					this.homeSelect()
				}
				if (tab._props.label == '充值排行榜') {
					this.type = 7
					this.homeSelect()
				}
				if (tab._props.label == '钱包排行榜') {
					this.type = 8
					this.flag = ''
					this.info.stockDate = this.getNowTime()
					this.homeSelect()
				}
				if (tab._props.label == '订单排行榜') {
					this.type = 9
					this.homeSelect()
				}
			},
			// 信息数据
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('invite/getAgentRanking'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						// 'type': this.type,
						// 'time': this.info.stockDate,
						// 'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data

					console.log(data.data.state)
					this.homeData = data.data
				})
			},

			// 详情跳转
			updatesvideo(row) {
				if (row.userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: row.userId
						}
					})
				} else {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '未绑定用户',
						type: 'warning'
					})
					return
				}

			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.homeSelect()
			},
			// 重置
			cleans() {
				this.address = ''
				this.flag = ''
				this.info.stockDate = this.getNowTime()
				this.homeSelect()
			},
			// 订单分析选择日期
			animeOrder() {
				console.log('info', this.info)
				this.homeSelect()
				// this.colonel()
			},
			// 订单分析年月日
			orderfenxi(value) {
				this.page = 1
				let vanumber = value
				this.flag = value
				this.homeSelect()

			},
		},
		mounted() {
			this.homeSelect()
		}
	}
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

	.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;

	}

	.tj {
		padding: 6px !important;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}
</style>
