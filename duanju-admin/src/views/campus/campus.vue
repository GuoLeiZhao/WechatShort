<template>
	<div>
		<div style="display: inline-block;float: right;">
			<el-button size="mini" type="primary" icon="document" @click="refresh" >刷新</el-button>
			<el-button style='margin:0 0 20px 10px;' :disabled="!isAuth('campus:add')" size="mini" type="primary" icon="document" @click="classifyStair()" >添加社区</el-button>
			<el-button style='margin:0 0 20px 10px;' size="mini" type="danger" icon="document" @click="deleteStairs()" :disabled="checkBoxData.length <= 0 || !isAuth('campus:delete')">批量删除</el-button>
		</div>
		<el-table
		@selection-change="changeFun"
		v-loading="tableDataLoading"
		  :data="tableData.list">
		  <el-table-column type="selection" fixed >
		  </el-table-column>
		   <el-table-column
			prop="campusId"
			label="编号">
		    </el-table-column>
			<el-table-column
				prop="campusName"
				label="社区名称">
			 </el-table-column>
			<el-table-column
				prop="campusDetails"
				label="社区地址">
			 </el-table-column>
			<el-table-column
		    label="操作"
			width="150">
		    <template slot-scope="scope">
				<el-button
				  size="mini"
				  type="primary"
				  :disabled="!isAuth('campus:update')"
				  @click="compile(scope.$index, scope.row)">编辑
				</el-button>
				<el-button
				  size="mini"
				  type="danger"
				 :disabled="!isAuth('campus:delete')"
				  @click="deleteStair(scope.row)">删除
				</el-button>
		    </template>
		  </el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
		  <el-pagination
		    @size-change="handleSizeChange"
		    @current-change="handleCurrentChange"
		    :page-sizes="[10, 20, 30, 40]"
		    :page-size="limit"
		    :current-page="page"
		    layout="total,sizes, prev, pager, next,jumper"
		    :total="tableData.totalCount">
		  </el-pagination>
		</div>
		<!-- 添加社区 -->
		<el-dialog title="添加社区" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">社区名称：</span>
				<el-input style="width:50%;" v-model="campusName" type="text" placeholder="请输入社区名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">社区地址：</span>
				<el-input style="width:50%;" v-model="campusDetails" type="text" placeholder="请输入社区地址"></el-input>
			</div>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="dialogFormVisible = false">取 消</el-button>
		    <el-button type="primary" @click="StairNoticeTo()">确 定</el-button>
		  </div>
		</el-dialog>
		<!-- 修改一级分类 -->
		<el-dialog title="修改社区" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
			  <el-form-item label="社区名称：" :label-width="formLabelWidth" >
			    <el-input v-model="form.campusName" style="width:65%;"></el-input>
			  </el-form-item>
			  <el-form-item label="社区地址：" :label-width="formLabelWidth" >
			    <el-input v-model="form.campusDetails" style="width:65%;"></el-input>
			  </el-form-item>
			</el-form>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="dialogFormVisible1 = false">取 消</el-button>
		    <el-button type="primary" @click="CompileNoticeTo()">确 定</el-button>
		  </div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
	    data() {
	      return {
				limit:10,
				page:1,
				checkBoxData: [],//多选框选择的值
				formLabelWidth:'200px',
				tableDataLoading:false,
				dialogFormVisible:false,
				dialogFormVisible1:false,
				tableData:[],
				campusName:'',
				campusDetails:'',
				form:{
					campusId:'',
					campusName:'',
					campusDetails:''
				}
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			// 刷新
			refresh(){
				this.dataSelect()
			},
			// 添加社区弹框
			classifyStair(){
				this.dialogFormVisible = true
			},
			// 添加社区
			StairNoticeTo(){
				if (this.campusName == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入社区名称',
				        type: 'warning'
				    });
				    return
				}
				if (this.campusDetails == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入社区地址',
				        type: 'warning'
				    });
				    return
				}
				this.$http({
				  url: this.$http.adornUrl('helpCampus/insertCampus'),
				  method: 'post',
				  data: this.$http.adornData({
					'campusName':this.campusName,
				    'campusDetails': this.campusDetails,
				  })
				}).then(({data}) => {
					this.dialogFormVisible = false
				    this.$message({
				      message: '添加成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
						 this.campusName = ''
						 this.campusDetails = ''
				        this.dataSelect()
				      }
				    })
				})
			},
			// 修改社区
			compile(index,rows){
				this.dialogFormVisible1=true;
				this.form.campusId = rows.campusId;
				this.form.campusName = rows.campusName;
				this.form.campusDetails = rows.campusDetails;
			},
			// 修改社区
			CompileNoticeTo(){
				this.$http({
				  url: this.$http.adornUrl('helpCampus/updateCampus '),
				  method: 'post',
				  data: this.$http.adornData({
					'campusId':this.form.campusId,
					'campusName': this.form.campusName,
					'campusDetails' :this.form.campusDetails
				  })
				}).then(({data}) => {
					if(data.code == 0){
						this.$message({
						  message: '操作成功',
						  type: 'success',
						  duration: 1500,
						  onClose: () => {
							  this.dialogFormVisible1 = false
							  this.dataSelect()
						  }
						})
					}else{
						this.$message({
						  message: data.msg,
						  type: 'error',
						  duration: 1500,
						  onClose: () => {
							  this.dialogFormVisible1 = false
							  this.dataSelect()
						  }
						})
					}
				})
			},
			// 批量删除
			deleteStairs(id){
				this.$confirm(`确定批量删除信息?`, '提示', {
				  confirmButtonText: '确定',
				  cancelButtonText: '取消',
				  type: 'warning'
				}).then(() => {
				  var ids= id ? [id] : this.checkBoxData.map(item => {
				  	return item.campusId
				  })
				  this.$http({
				    url: this.$http.adornUrl(`helpCampus/deleteCampus?ids=${ids}`),
				    method: 'get',
				    params: this.$http.adornParams({
				    })
				  }).then(({data}) => {
				      if(data.code == 0){
				      	this.$message({
				      	  message: '删除成功',
				      	  type: 'success',
				      	  duration: 1500,
				      	  onClose: () => {
				      		this.content= ''
				  			this.status  = 0
				      	    this.dataSelect()
				      	  }
				      	})
				      }else{
				      	this.$message({
				      	  message: data.msg,
				      	  type: 'error',
				      	  duration: 1500,
				      	  onClose: () => {
				      		this.content= ''
				  			this.status  =  0
				      	    this.dataSelect()
				      	  }
				      	})
				      }
				  })
				}).catch(() => {})
			},
			//删除
			deleteStair(row){
				this.$confirm(`确定删除此条信息?`, '提示', {
				  confirmButtonText: '确定',
				  cancelButtonText: '取消',
				  type: 'warning'
				}).then(() => {
				  this.$http({
				    url: this.$http.adornUrl(`helpCampus/deleteCampus?ids=${row.campusId}`),
				    method: 'get',
				    params: this.$http.adornParams({
				    })
				  }).then(({data}) => {
					  if(data.code == 0){
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
							  this.dataSelect()
							}
						})  
					  }else{
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
							  this.dataSelect()
							}
						})   
					  }
					 
				  })
				}).catch(() => {})
			},
			// 重置
			cleans(){
				this.phone = ''
				this.status = 0
				this.dataSelect()
			},
			// 获取派单数据列表
			dataSelect () {
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl('helpCampus/selectCampusPage'),
				method: 'get',
				params: this.$http.adornParams({
					'page':this.page,
					'limit':this.limit,
					'content':this.content
				})
			  }).then(({data}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
			  })
			}
		},
		mounted() {
			this.dataSelect()
		}
	  };
</script>

<style>
	
</style>
