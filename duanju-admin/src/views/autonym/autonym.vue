<template>
  <div>
	  <div style="display: inline-block;">
	  	<span>状态：</span>
	  	<el-select v-model="status" style="width:150px;margin-left: 10px;" @change="select()">
	  	  <el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
	  	  </el-option>
	  	</el-select>&nbsp;&nbsp;
	  	<div style="position: relative;display: inline-block;">
	  		<span>姓名：</span>
	  	  <el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入姓名" v-model="userName"></el-input>&nbsp;&nbsp;
	  	</div>
	  	<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询</el-button>
	  	<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
	  	<el-button style='margin:0 0 20px 20px;' v-if="isAuth('autonym:pass')" size="mini" type="primary" icon="document" @click="passClick()" :disabled="checkBoxData.length <= 0">通过</el-button>
	  </div>
	  <el-table
	  @selection-change="changeFun"
	  	v-loading="tableDataLoading"
	  	:data="tableData.list">
		<el-table-column type="selection" fixed >
		</el-table-column>	
	  	<el-table-column
	  	 prop="id"
	  	 label="编号">
	  	 </el-table-column>
	  	</el-table-column>
	  	<el-table-column
	  	 prop="studentImg"
	  	 label="学生证照片"
		 width="150">
	  	 <template slot-scope="scope">
	  	   <div v-if="scope.row.studentImg == null || scope.row.studentImg == ''">
	  	     暂无图片
	  	   </div>
	  	   <div v-else-if="scope.row.studentImg.includes(',')" style="display:flex;flex-wrap: wrap;">
	  	      <div v-for="item in scope.row.studentImg.split(',')" style="margin: 2px;">
	  	        <el-popover placement="top-start" title="" trigger="hover">
	  	            <img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
	  	            <img style="width: 200px; height: 200px" :src="item" alt="">
	  	         </el-popover>
	  	      </div>
	  	   </div>
	  	   <div v-else>
	  	     <el-popover placement="top-start" title="" trigger="hover">
	  	         <img style="width: 50px; height: 50px" :src="scope.row.studentImg" alt="" slot="reference">
	  	         <img style="width: 200px; height: 200px" :src="scope.row.studentImg" alt="">
	  	      </el-popover>
	  	   </div>
	  	 </template>
	  	 </el-table-column>
		 <el-table-column
		  prop="cardFront"
		  label="身份证正面"
		  width="150">
		  <template slot-scope="scope">
		    <div v-if="scope.row.cardFront == null || scope.row.cardFront == ''">
		      暂无图片
		    </div>
		    <div v-else-if="scope.row.cardFront.includes(',')" style="display:flex;flex-wrap: wrap;">
		       <div v-for="item in scope.row.cardFront.split(',')" style="margin: 2px;">
		         <el-popover placement="top-start" title="" trigger="hover">
		             <img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
		             <img style="width: 200px; height: 200px" :src="item" alt="">
		          </el-popover>
		       </div>
		    </div>
		    <div v-else>
		      <el-popover placement="top-start" title="" trigger="hover">
		          <img style="width: 50px; height: 50px" :src="scope.row.cardFront" alt="" slot="reference">
		          <img style="width: 200px; height: 200px" :src="scope.row.cardFront" alt="">
		       </el-popover>
		    </div>
		  </template>
		  </el-table-column>
		  <el-table-column
		   prop="cardBack"
		   label="身份证反面"
		   width="150">
		   <template slot-scope="scope">
		     <div v-if="scope.row.cardBack == null || scope.row.cardBack == ''">
		       暂无图片
		     </div>
		     <div v-else-if="scope.row.cardBack.includes(',')" style="display:flex;flex-wrap: wrap;">
		        <div v-for="item in scope.row.cardBack.split(',')" style="margin: 2px;">
		          <el-popover placement="top-start" title="" trigger="hover">
		              <img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
		              <img style="width: 200px; height: 200px" :src="item" alt="">
		           </el-popover>
		        </div>
		     </div>
		     <div v-else>
		       <el-popover placement="top-start" title="" trigger="hover">
		           <img style="width: 50px; height: 50px" :src="scope.row.cardBack" alt="" slot="reference">
		           <img style="width: 200px; height: 200px" :src="scope.row.cardBack" alt="">
		        </el-popover>
		     </div>
		   </template>
		   </el-table-column>
	  	 <el-table-column
			prop="userName"
			label="姓名"
			width="200">
			<template slot-scope="scope">
			  <span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
			</template>
		</el-table-column>
	  	<el-table-column
	  	prop="school"
	  	label="学校"
	  	width="150">
	  	</el-table-column>
	  	<el-table-column
	  	prop="address"
	  	label="地址"
	  	width="160">
	  	</el-table-column>
		<el-table-column
		prop="content"
		label="审核内容"
		width="160">
		</el-table-column>
	  	<el-table-column
	  	prop="createTime"
	  	label="创建时间"
	  	width="160">
	  	</el-table-column>
	  	<el-table-column
		 fixed='right'
	  	prop="status"
	  	label="状态"
	  	width="160">
	  	<template slot-scope="scope">
	  		<span v-if="scope.row.status == 0">待审核</span>
	  		<span v-if="scope.row.status == 1">审核成功</span>
	  		<span v-if="scope.row.status == 2">已拒绝</span>
	  	</template>
	  	</el-table-column>
	  	<el-table-column
	  		fixed='right'
	  		label="操作"
	  		width="150">
	  		<template slot-scope="scope">
	  			<el-button
	  				size="mini"
	  				type="primary"
					:disabled="!isAuth('autonym:refuse') || scope.row.status !== 0"
	  				 @click="refuseClick(scope.row)">拒绝
	  			</el-button>
	  			<el-button
	  				size="mini"
	  				type="primary"
					:disabled="!isAuth('autonym:pass') || scope.row.status !== 0"
	  				 @click="passClicks(scope.row)">通过
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
	  <!-- 任务拒绝弹框 -->
	  <el-dialog title="任务拒绝" :visible.sync="dialogFormVisible" center>
	  	<div style="margin-bottom: 10px;">
	  		<span style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">拒绝理由：</span>
	  		<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入拒绝理由"></el-input>
	  	</div>
	    <div slot="footer" class="dialog-footer">
	      <el-button @click="dialogFormVisible = false">取 消</el-button>
	      <el-button type="primary" @click="refuseto()">确 定</el-button>
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
				content:'',
				status:-1,
				statesnum:[
					{
					  value: -1,
					  label: '全部'
					},
					{
					  value: 0,
					  label: '待审核'
					}, {
					  value: 1,
					  label: '审核成功'
					}, {
					  value: 2,
					  label: '拒绝'
					}
				],
				userName :'',
				activeName: 'first',
				tableDataLoading:false,
				dialogFormVisible:false,
				tableData:[],
				checkBoxData: [],//多选框选择的值
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
			},
			// 详情跳转
			updates (row) {
			  this.$router.push({path: '/userDetail', query: {userId: row.userId}})
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			//任务通过
			passClick(id){
				this.status  = 1
				this.content= '同意'
				var ids= id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$http({
				  url: this.$http.adornUrl(`student/auditStudentAuthentication/${ids}/${this.status}/${this.content}`),
				  method: 'post',
				  data: this.$http.adornData({
				  })
				}).then(({data}) => {
				    if(data.code == 0){
				    	this.$message({
				    	  message: '操作成功',
				    	  type: 'success',
				    	  duration: 1500,
				    	  onClose: () => {
				    		this.content= ''
							this.status  = -1
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
							this.status  =  -1
				    	    this.dataSelect()
				    	  }
				    	})
				    }
				})
			},
			//单条任务通过
			passClicks(row){
				this.status = 1
				this.content= '同意'
				this.$http({
				  url: this.$http.adornUrl(`student/auditStudentAuthentication/${row.id}/${this.status}/${this.content}`),
				  method: 'post',
				  data: this.$http.adornData({
				  })
				}).then(({data}) => {
					if(data.code == 0){
						this.$message({
						  message: '操作成功',
						  type: 'success',
						  duration: 1500,
						  onClose: () => {
							this.content= ''
							this.status  = -1
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
							this.status  = -1
						    this.dataSelect()
						  }
						})
					}
				    
				})
			},
			//任务拒绝
			refuseClick(rows){
				this.status = 2
				this.helpTakeId = rows.id
				this.content = ''
				this.dialogFormVisible = true
			},
			refuseto(){
				if (this.content == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入拒绝理由',
				        type: 'warning'
				    });
				    return
				}
				this.$http({
				  url: this.$http.adornUrl(`student/auditStudentAuthentication/${this.helpTakeId}/${this.status}/${this.content}`),
				  method: 'post',
				  data: this.$http.adornData({
				  })
				}).then(({data}) => {
					this.dialogFormVisible = false
				    this.$message({
				      message: '操作成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
						this.content = ''
						this.status  = -1
				        this.dataSelect()
				      }
				    })
				})
			},
			// 查询
			select(){
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans(){
				this.status = -1
				this.userName = ''
				this.dataSelect()
			},
			// 获取派单数据列表
			dataSelect () {
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl('student/selectStudentPage'),
				method: 'get',
				params: this.$http.adornParams({
					'page':this.page,
					'limit':this.limit,
					'status':this.status,
					'userName':this.userName
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
