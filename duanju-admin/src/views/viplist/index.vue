<template>
	<el-tabs v-model = "activeName" @tab-click = "handleClick">
		<!-- <el-tab-pane label = "会员列表" name = "first"> -->
			<div style = "margin-right:2%;text-align: right;">
			  <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" :disabled="!isAuth('viplist:add')" @click="add">添加
			  </el-button>
			</div>
			<el-table v-loading = "tableDataLoading" :data = "tableData.list" >
			<el-table-column fixed prop = "id" label = "编号" width = "50"></el-table-column>
			<el-table-column prop = "vipNameType" label = "会员类型" width = "150">
				<template slot-scope = "scope">
					<span v-if = "scope.row.vipNameType == 0">月会员</span>
					<span v-if = "scope.row.vipNameType == 1">季会员</span>
					<span v-if = "scope.row.vipNameType == 2">年会员</span>
				</template>
			</el-table-column>
			<el-table-column prop = "money" label = "会员价格" >
			</el-table-column>
			  	<el-table-column label = "操作" width = "180" align="center" fixed="right">
					<template slot-scope = "scope">
						<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" :disabled="!isAuth('viplist:update')" @click="updata(scope.row)">修改
						</el-button>
						<el-button size = "mini" type = "danger" :disabled="!isAuth('viplist:delete')" @click = "deletes(scope.row)">删除</el-button>
					</template>
			  	</el-table-column>
			</el-table>
			<div style = "text-align: center;margin-top: 10px;float:right">
			  	<el-pagination @size-change = "handleSizeChange" @current-change = "handleCurrentChange" :page-sizes = "[5, 10, 15, 20]"
							   :page-size = "limit" :current-page = "page" layout = "total,sizes, prev, pager, next,jumper"
							   :total = "tableData.totalCount">
			  	</el-pagination>
			</div>
		<!-- </el-tab-pane> -->
		<el-dialog title="添加" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
			  <span style="width: 200px;display: inline-block;text-align: right;">会员类型：</span>
			  <el-radio-group v-model="vipNameType">
				<el-radio :label="0">月会员</el-radio>
			    <el-radio :label="1">季会员</el-radio>
			    <el-radio :label="2">年会员</el-radio>
			  </el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">会员价格：</span>
				<el-input style="width:50%;" v-model="money" type="number" min="0" placeholder="请输入会员价格" ></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="vipAdd()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<div style="margin-bottom: 10px;">
			  <span style="width: 200px;display: inline-block;text-align: right;">会员类型：</span>
			  <el-radio-group v-model="vipNameType">
			    <el-radio :label="0">月会员</el-radio>
			    <el-radio :label="1">季会员</el-radio>
			    <el-radio :label="2">年会员</el-radio>
			  </el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">会员价格：</span>
				<el-input style="width:50%;" v-model="money" type="number" min="0" placeholder="请输入会员价格"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="vipUpdata()">确 定</el-button>
			</div>
		</el-dialog>
	</el-tabs>

</template>
<script>
    export default {
		data () {
			return {
				activeName:'first',
				page: 1,
				limit: 10,
				classify: 1,
				id:'',
				vipNameType: -1,
				money: '',
				tableDataLoading:false,
				dialogFormVisible:false,
				dialogFormVisible1:false,
				tableData:{},
			}
		},
		methods: {
			// 多选
			changeFun (val) {
				this.checkBoxData = val
			},
			// tabs切换
			handleClick (tab, event) {
				if (tab._props.label == '轮播图') {
					this.page = 1
					this.limit = 5
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
			// 添加
			add(){
				this.vipNameType = -1
				this.money = ''
				this.id = ''
				this.dialogFormVisible = true
			},
			updata(row){
				this.vipNameType = row.vipNameType
				this.money = row.money
				this.id = row.id
				this.dialogFormVisible1 = true
			},
			vipAdd(){
				if (this.vipNameType == -1) {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择会员类型',
				        type: 'warning'
				    });
				    return
				}
				if (this.money == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入会员价格',
				        type: 'warning'
				    });
				    return
				}
				this.$http({
				  url: this.$http.adornUrl('vipDetails/insertVipDetails'),
				  method: 'post',
				  data: this.$http.adornData({
					  'vipNameType':this.vipNameType,
					  'money':this.money
				  })
				}).then(({data}) => {

				  if(data.code == 0){
				    this.$message({
				      message: '操作成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
						  this.dialogFormVisible = false
				        this.vipNameType = ''
				        this.money = ''
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
			vipUpdata(){
				if (this.money == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入会员价格',
				        type: 'warning'
				    });
				    return
				}
				this.$http({
				  url: this.$http.adornUrl('vipDetails/updateVipDetails'),
				  method: 'post',
				  data: this.$http.adornData({
					  'vipNameType':this.vipNameType,
					  'money':this.money,
					  'id':this.id
				  })
				}).then(({data}) => {

				  if(data.code == 0){
					  this.dialogFormVisible1 = false
				    this.$message({
				      message: '操作成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
				        this.vipNameType = ''
				        this.money = ''
						this.id = ''
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
			// 删除banner图
			deletes (row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`vipDetails/deleteVipDetails?id=${delid}`),
						method: 'post',
						params: this.$http.adornParams({})
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
				this.$http({
					url: this.$http.adornUrl('vipDetails/selectVipDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit
					})
				}).then(({data}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData
					}
				})
			}
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
