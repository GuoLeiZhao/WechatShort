<template>
	<div>
		<div style="display: inline-block;">
			<!-- <span>地区：</span>
			<el-select v-model="indentState" style="width:150px;margin-left: 10px;" @change="select(indentState)">
				<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
				</el-option>
			</el-select>&nbsp;&nbsp; -->
			<div style="position: relative;display: inline-block;">
				<span>昵称：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入昵称" v-model="address">
				</el-input>&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;">
				<span>电话：</span>
				<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入电话" v-model="phone">
				</el-input>&nbsp;&nbsp;
			</div>
			
			<!-- <div style="display: inline-block;">
				<span>时间类型：</span>
				<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="orderfenxi">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 300px;margin-left: 10px;" v-model="info.stockDate" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
					@change="animeOrder">
				</el-date-picker>
			</div> -->
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询
			</el-button>
			<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置
			</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="homeData.records">
			<el-table-column fixed prop="userId" label="排名" width="80">
				<!-- <template slot-scope="scope">
					<div>{{scope.$index+1}}</div>
				</template> -->
			</el-table-column>
			<el-table-column prop="userName" label="昵称" width="120">
				<template slot-scope="scope">
					<div style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row)">{{scope.row.userName?scope.row.userName:'未绑定'}}
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="phone" label="电话" >
			</el-table-column>
			<el-table-column prop="money" label="收益" >
			</el-table-column>
			<el-table-column prop="counts" label="邀请人数">
			</el-table-column>
			<!-- <el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column> -->
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
				:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="homeData.total">
			</el-pagination>
		</div>

	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../locality/quill-config.js'
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var cityOptions = []
	var geocoder, map, markersArray = [];
	export default {
		components: {
			quillEditor
		},
		data() {
			return {
				size: 10,
				page: 1,
				state: '',
				limit: 10,
				classify: 6,
				classifys: 6,
				title: '',
				type: '',
				nav: '',
				name: '',
				keyword: '',
				describes: '',
				checkBoxData: [], //多选框选择的值
				method: 'false',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: true,
				tableDataLoading4: false,
				dialogFormVisible3: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				dialogFormVisible9: false,
				homeData: {},
				homeData1: [{
						title: '帮我送',
						id: 1
					},
					{
						title: '帮我取',
						id: 2
					},
				],
				choicenData2: [],
				choicenData: [],

				url: '',
				imageUrl: '',
				imageUrl2: '',
				id: '',
				bannerData: [],
				form1: {
					// id: '',
					// url: '',
					// imageUrl: ''
				},
				formcomp: {
					id: '',
					state: '',
					title: '',
					url: '',
					imageUrl: ''
				},
				userId: '',
				search: '',
				myPhone: '',
				classifyIds: 0,
				statusId: 1,

				homeData2: [],
				quillOption: quillConfig,
				statusIdd: 1,
				statesnum: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '待支付'
					}, {
						value: 2,
						label: '待接单'
					}, {
						value: 3,
						label: '已接单'
					}, {
						value: 4,
						label: '已完成'
					}, {
						value: 5,
						label: '已取消'
					}
				],
				// x新的
				indentType: 3,
				deliveryUserPhone: '', //收货人手机号
				indentState: 0, //任务状态
				flag: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				flags: [{
					value: 'day',
					label: '按天查询'
				}, {
					value: 'month',
					label: '按月查询'
				}, {
					value: 'year',
					label: '按年查询'
				}],
				address:'',
				phone:'',
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
				this.indentState = 0
				this.phone = ''
				if (tab._props.label == '同城帮买') {
					this.indentType = 3
					this.homeSelect()
				}
				if (tab._props.label == '同城服务') {
					this.type = 1
					this.indentType = 4
					this.homeSelect()
				}
				if (tab._props.label == '送取任务') {
					this.indentType = 1
					this.homeSelect()
				}
			},
			// 获取社区数据列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectInviteUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userName': this.address,
						'phone': this.phone,
						// 'date': this.info.stockDate,
						// 'dateType': this.flag,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.homeData = data.data
				})
			},
			// 信息数据
			InformationSelect() {
				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl(`information/selectInformationList`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'search': this.search,
						'classify': this.classifyIds,
						'status': this.statusId,
						'phone': this.myPhone,
						'campus': this.campus1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.records) {
						if (data.data.records[i].img) {
							data.data.records[i].imgs = data.data.records[i].img.split(',')
						}

					}
					let returnData = data.data
					this.choicenData = returnData


				})
			},

			// 审核
			shenhe(row) {
				this.shenheId = row.id
				this.dialogFormVisible8 = true
			},
			// 提交审核
			refuseto(row) {
				if (this.radio == 2 && this.contents == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入驳回理由',
						type: 'warning'
					})
					return
				} else {
					let ids = this.shenheId
					let status = this.radio
					let content = this.contents
					this.$http({
						url: this.$http.adornUrl(
							`information/auditInformation?ids=${ids}&status=${status}&content=${content}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.dialogFormVisible8 = false
						this.$message({
							message: '审核完成',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.radio = 1
								this.contents = ''
								this.InformationSelect()
							}
						})
					})
				}
			},
			// 修改信息弹框
			choiCompile(index, row) {
				console.log(index, row)

			},
			// 删除信息
			choidelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`information/deleteInformationById?ids=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.InformationSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 筛选信息
			animeDat2(classifyIds) {
				this.classifyIds = classifyIds
				this.homeSelect()
			},
			animeDat3(state) {
				this.InformationSelect()
			},
			// 多选
			handleSelectionChange(val) {

				var arr = []
				for (var i in val) {
					arr.push(val[i].id)
				}
				this.multipleSelection = arr;
				console.log('val', val, this.multipleSelection)
			},
			// 批量删除
			choideletes() {
				console.log(this.multipleSelection)
				let delid = this.multipleSelection.toString()
				this.$confirm(`确定删除信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`information/deleteInformationById?ids=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.InformationSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取社区数据列表
			homeSelect1() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('helpCampus/selectCampusList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.homeData2 = returnData
				})
			},
			// 分类是否启用
			change2(val, row) {
				this.$http({
					url: this.$http.adornUrl(`activity/updateActivityStatus?id=${row.id}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.homeSelect()
						}
					})
				})
			},

			// 任务上下架
			change3(val, row) {
				this.$http({
					url: this.$http.adornUrl(`information/updateInformationStatus?id=${row.id}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.homeSelect()
						}
					})
				})
			},
			// 详情跳转
			updatesvideo(row) {
				if(row.userId){
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: row.userId
						}
					})
				}else{
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
				this.indentState = 0
				this.address = ''
				this.phone = ''
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
				// if (vanumber === 1) {
				this.flag = value
				this.homeSelect()
				// this.colonel()
				// }
				// if (vanumber === 2) {
				// 	this.flag = 2
				// 	this.taskData()
				// 	this.colonel()
				// }
				// if (vanumber === 3) {
				// 	this.flag = 3
				// 	this.taskData()
				// 	this.colonel()
				// }
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
