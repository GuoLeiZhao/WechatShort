<template>
	<div>
		<div style="display: inline-block;font-size:18px;margin-bottom: 15px;">
			<a href="#" @click="prev" style="text-decoration:none;font-size: 14px;">
				<icon-svg name="jiantou" style="width: 1.2em;height: 1.2em;position: relative;top: 0.3em;"></icon-svg>
				返回
			</a>
			<span style="display: inline-block;margin: 0 15px;color: #D9D9D9;">|</span>
			<span>活动详情</span>
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData.list">
			<el-table-column prop="nickName" label="昵称" width="150">
				<template slot-scope="scope">
					<span
						style="color: #4f9dec;cursor: pointer;">{{scope.row.nickName ? scope.row.nickName : '未绑定'}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="integralName" label="活动名称" width="150">
			</el-table-column>
			<el-table-column prop="describes" label="活动描述">
			</el-table-column>
			<el-table-column prop="money" label="活动金额">
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
			</el-pagination>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				tableDataLoading: true,
				tableData: []
			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
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
			// 获取数据列表
			dataSelect() {
				let sendMoneyId = this.$route.query.sendMoneyId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('integral/selectSendMoneyDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'sendMoneyId': sendMoneyId,
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
