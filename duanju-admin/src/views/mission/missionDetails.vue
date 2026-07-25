<template>
	<div class="detailtabel">
		<div style="display: inline-block;font-size:18px;margin-bottom: 15px;">
			<a href="#" @click="prev" style="text-decoration:none;font-size: 14px;">
				<icon-svg name="jiantou" style="width: 1.2em;height: 1.2em;position: relative;top: 0.3em;"></icon-svg>
				返回
			</a>
			<span style="display: inline-block;margin: 0 15px;color: #D9D9D9;">|</span>
			<span>派单详情</span>
		</div>
		<div class="table_main">
			<table>
				<tbody>
					<tr>
						<th>用户名称</th>
						<td>{{tableData.nickName}}</td>
						<th>标题</th>
						<td>{{tableData.title}}</td>
						<th style="width: 180px;">任务要求</th>
						<td class="border-rt">{{tableData.content}}</td>
					</tr>
					<tr>
						<th>验证信息</th>
						<td>{{tableData.verifyContent}}</td>
						<th>打开内容</th>
						<td style="width: 180px;">{{tableData.openContent}}</td>
						<th>编号</th>
						<td class="border-rt">{{tableData.id}}</td>
					</tr>
					<tr>
						<th>数量</th>
						<td>{{tableData.taskNum}}</td>
						<th>接单数量</th>
						<td>{{tableData.endNum}}</td>
						<th>发布时间</th>
						<td class="border-rt">{{tableData.createTime}}</td>
					</tr>
					<tr>

						<th>截至时间</th>
						<td>{{tableData.endTime}} </td>
						<th>审核时间（按分）</th>
						<td>{{tableData.auditTime}}</td>
						<th>任务限时（按分）</th>
						<td class="border-rt">{{tableData.restrictTime}}</td>
					</tr>
					<tr>
						<th>状态</th>
						<td v-if="tableData.state == 0">待审核</td>
						<td v-if="tableData.state == 1">进行中</td>
						<td v-if="tableData.state == 2">已结束</td>
						<td v-if="tableData.state == 3">已拒绝</td>
						<td v-if="tableData.state == 4">已结算</td>
						<th>打开方式</th>
						<td>
							<span v-if="tableData.openType== 1">链接打开</span>
							<span v-if="tableData.openType== 2">口令打开</span>
							<span v-if="tableData.openType== 3">扫码打开</span>
						</td>
						<th>打开app分类</th>
						<td class="border-rt">{{tableData.openApp}}</td>
					</tr>
					<tr>
						<th class="border-bt">验证信息</th>
						<td class="border-bt">{{tableData.verifyContent}}</td>
						<th class="border-bt">价格(原价)</th>
						<td class="border-bt">{{tableData.taskOriginalPrice}}</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div style="margin-top:25px;">
			<el-table v-loading="tableDataLoading" :data="taskData.list">
				<el-table-column fixed prop="id" label="接单id">
				</el-table-column>
				<el-table-column prop="nickName" label="接单人名称" width="150">
				</el-table-column>
				<el-table-column prop="content" label="提交内容" width="150">
					<template slot-scope="scope">
						<span>{{scope.row.content}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="money" label="价格(接单时收取的价格)" width="180">
				</el-table-column>
				<el-table-column prop="zhiMoney" label="直属收益">
				</el-table-column>
				<el-table-column prop="feiMoney" label="非直属收益" width="150">
				</el-table-column>
				<el-table-column prop="pingMoney" label="平台收益">
				</el-table-column>
				<el-table-column prop="auditContent" label="拒绝原因" width="160">
				</el-table-column>
				<el-table-column prop="category" label="拒绝类型" width="160">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="auditTime" label="审核时间" width="160">
				</el-table-column>
				<el-table-column fixed='right' prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<span style="color: #4f9dec;" v-if="scope.row.state === 0 ">接单成功</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 1 ">提交待审核</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 2 ">审核成功</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 3 ">已拒绝</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 4 ">已维权</span>
						<span style="color: #4f9dec;" v-if="scope.row.state === 5 ">已放弃</span>
					</template>
				</el-table-column>
				<el-table-column fixed='right' label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="check(scope.$index, scope.row)">查看
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="taskData.totalCount">
				</el-pagination>
			</div>
			<!-- 任务查看弹框 -->
			<el-dialog title="查看" :visible.sync="dialogFormVisible" center>
				<div v-for="(item,index) in helpTaskData" :key="index" style="text-align: center;">
					<span>步骤 {{index+1}}</span>
					<!-- <p>接单id：{{item.helpSendOrderId}}</p> -->
					<p class="red">验证图</p>
					<img :src="item.picture" alt="" width="260" height="460">
				</div>
				<div v-if="helpTaskData == '' " style="text-align: center;">暂无内容</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
				</div>
			</el-dialog>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				state: 0,
				limit: 5,
				page: 1,
				dialogFormVisible: false,
				tableDataLoading: true,
				tableData: {},
				taskData: [],
				helpTaskData: []
			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			handleSizeChange(val) {
				this.limit = val;
				this.taskSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.taskSelect()
			},
			// 接单人查看
			check(index, rows) {
				this.$http({
					url: this.$http.adornUrl('helpTask/selectSendOrderDetailsById'),
					method: 'get',
					params: this.$http.adornParams({
						'id': rows.id
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						let returnData = data.data;
						this.dialogFormVisible = true
						this.helpTaskData = returnData
					}
				})

			},
			// 获取数据列表
			dataSelect() {
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl('helpTask/selectTakeOrderDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'id': id
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						let returnData = data.data;
						this.tableData = returnData;
					}
				})
			},
			// 获取数据列表
			taskSelect() {
				this.tableDataLoading = true
				let page = this.page - 1
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl('helpTask/selectSendOrderByTaskList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'limit': this.limit,
						'id': id
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.taskData = returnData;
					}
				})
			},
		},
		mounted() {
			this.dataSelect()
			this.taskSelect()
		},
	}
</script>


<style scoped="scoped">
	.detailtabel h2 {
		margin-top: 0;
	}

	.detailtabel .table_main table {
		width: 100%;
	}

	.detailtabel .table_main {
		border: 1px solid #e8e8e8;
	}

	.detailtabel table tr {
		border-bottom: 1px solid #e8e8e8;
	}

	.detailtabel table tr th {
		background-color: #fafafa;
		padding: 16px 24px;
		border-right: 1px solid #e8e8e8;
		border-bottom: 1px solid #e8e8e8;
	}

	.detailtabel table tr td {
		padding: 16px 24px;
		border-right: 1px solid #e8e8e8;
		border-bottom: 1px solid #e8e8e8;
	}

	.detailtabel table th {
		color: rgba(0, 0, 0, .85);
		font-weight: 400;
		font-size: 14px;
		line-height: 1.5;
	}

	.border-rt {
		border-right: none !important;
	}

	.border-bt {
		border-bottom: none !important;
	}
</style>
