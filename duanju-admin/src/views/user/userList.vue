<template>
	<div>
		<div style="margin:5px;display: inline-block;">
			<span>手机号:</span>
			<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
				v-model="phone"></el-input>
		</div>
		<div style="margin:5px;display: inline-block;">
			<span>昵称:</span>
			<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入昵称"
				v-model="userName"></el-input>
		</div>
		<div style="margin:5px;display: inline-block;">
			<span>邀请人邀请码:</span>
			<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请人邀请码"
				v-model="inviterCode"></el-input>
		</div>
		<div style="margin:5px;display: inline-block;">
			<span>渠道来源:</span>
			<el-select v-model="platformT" style="width:150px;" @change="select()">
				<el-option v-for="item in homeDataQ" :key="item.value" :label="item.label" :value="item.value">
				</el-option>
			</el-select>&nbsp;&nbsp;&nbsp;
		</div>
		<div style="display: inline-block;">
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
			</el-button>
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
			</el-button>
		</div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="全部用户" name="first">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userId" label="编号" width="80">
					</el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="rate" label="推广收益比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;" @click="xiugai(scope.row,'rate')">
								修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="渠道来源">
						<template slot-scope="scope">
							<span>{{ scope.row.platform ? scope.row.platform : '-' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhifubaoName ? scope.row.zhifubaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhifubao ? scope.row.zhifubao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.state,scope.row.userId)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="warning" @click="updateVip(scope.row)"
								:disabled="!isAuth('userList:updateVip')" v-if="scope.row.member!=2">设置会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)"
								:disabled="!isAuth('userList:updateVip')" v-if="scope.row.member==2" plain>取消会员
							</el-button>
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
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
			<el-tab-pane label="会员用户" name="second">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="rate" label="推广收益比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;" @click="xiugai(scope.row,'rate')">
								修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="渠道来源">
						<template slot-scope="scope">
							<span>{{ scope.row.platform ? scope.row.platform : '-' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhifubaoName ? scope.row.zhifubaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhifubao ? scope.row.zhifubao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.state,scope.row.userId)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="warning" @click="updateVip(scope.row)"
								:disabled="!isAuth('userList:updateVip')" v-if="scope.row.member!=2">设置会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)"
								:disabled="!isAuth('userList:updateVip')" v-if="scope.row.member==2" plain>取消会员
							</el-button>
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
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
			<el-tab-pane label="普通用户" name="third">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="rate" label="推广收益比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.rate?scope.row.rate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;" @click="xiugai(scope.row,'rate')">
								修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="渠道来源">
						<template slot-scope="scope">
							<span>{{ scope.row.platform ? scope.row.platform : '-' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhifubaoName ? scope.row.zhifubaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhifubao ? scope.row.zhifubao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.state,scope.row.userId)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="warning" @click="updateVip(scope.row)"
								:disabled="!isAuth('userList:updateVip')" v-if="scope.row.member!=2">设置会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)"
								:disabled="!isAuth('userList:updateVip')" v-if="scope.row.member==2" plain>取消会员
							</el-button>
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<!-- <el-button size = "mini" type = "danger" @click = "updates1(scope.row)">封号</el-button> -->
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
			<!-- 修改比例 -->
			<el-dialog :title="titleBl" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">比例：</span>
					<el-input style="width:50%;" v-model="proportion" type="number" :min="0" :controls="false"
						:placeholder="titleBl"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>
<script>
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 2,
				state: 'false',
				limit: 10,
				page: 1,
				phone: '',
				value: '',
				member: -1,
				name: '',
				tableDataLoading: true,
				tableData: [],
				platforms: [{
						value: 'all',
						label: '全部'
					}, {
						value: 'wap',
						label: '网站'
					}, {
						value: 'app',
						label: 'APP'
					}, {
						value: 'weixin',
						label: '微信公众号'
					},
					{
						value: 'mp',
						label: '微信小程序'
					}
				],
				campus: '',
				activeName: 'first',
				userName: '',
				inviterCode: '',
				platformT: '',
				homeDataQ: [{
					value: '',
					label: '全部'
				}, {
					value: 'H5',
					label: 'H5'
				}, {
					value: 'app',
					label: 'app'
				}, {
					value: '小程序',
					label: '小程序'
				}, ],
				dialogFormVisible2: false,
				titleBl: '修改佣金比例',
				proportion:'',
			}
		},
		methods: {
			// tabs切换
			handleClick(tab, event) {
				this.page = 1
				if (tab._props.label == '全部用户') {
					this.member = -1
					this.dataSelect()
				}
				if (tab._props.label == '会员用户') {
					this.member = 2
					this.dataSelect()
				}
				if (tab._props.label == '普通用户') {
					this.member = 1
					this.dataSelect()
				}

			},
			// 状态
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
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
			// 详情跳转
			updates(row) {
				console.log("`````", row.userId)
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 详情跳转
			updates2(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.superior
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
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.campus = ''
				this.phone = ''
				this.userName = ''
				this.inviterCode = ''
				this.platformT = ''
				this.dataSelect()
			},
			//删除用户
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
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
				})
			},
			// 封号
			updates1(row) {},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'member': this.member,
						'phone': this.phone,
						'campus': this.campus,
						'userName': this.userName,
						'inviterCode': this.inviterCode,
						'platform': this.platformT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 设置会员
			updateVip(row) {

				this.$http({
					url: this.$http.adornUrl('vipDetails/sendVip'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': row.userId,
						'num': 30
					})
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
								this.dataSelect()
							}
						})
					}

				})
			},
			// 取消会员
			quxiaoVip(row) {
				this.$confirm(`确定要取消会员?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('vipDetails/deleteVip'),
						method: 'post',
						// data: this.$http.adornData({
						params: this.$http.adornParams({
							'userId': row.userId
						})
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
			xiugai(row, text) {
				
				if (text == 'rate') {
					this.titleBl = '修改推广收益比例'
					this.proportion = row.rate
				}
				
				this.texts = text
				this.userIdss = row.userId
				this.dialogFormVisible2 = true
			},
			StairNoticeTo2() {
				var datas = {}
				if (this.texts == 'rate') {
					datas.rate = this.proportion
				}
				
				
				datas.userId = this.userIdss
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					console.log('data', data)
					if(data.code==0){
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible2 = false
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
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped="scoped">
	.el-button+.el-button {
		margin-left: 0 !important;
		margin-top: 5px !important;
	}
</style>
