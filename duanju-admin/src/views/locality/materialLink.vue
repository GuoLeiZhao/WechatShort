<template>
  <div>
		<div style = "float: right;margin-right:2%;">
		    <el-button style = "margin: 10px 0;" :disabled = "!isAuth('materialLink:add')" size = "mini" type = "primary" icon = "document"
                   @click = "advertNotice">添加素材</el-button>
		  </div>
		  <el-table v-loading = "tableDataLoading" :data = "guangaoData">
		    <el-table-column fixed prop = "id" label = "编号" width = "80"></el-table-column>
        <el-table-column prop = "imgUrl" label = "素材图片" width="180">
          <template slot-scope = "scope">
            <img :src = "scope.row.image_url" alt = "" width = "170px" height = "60">
          </template>
		    </el-table-column>
		    <el-table-column prop = "url" label = "素材链接"></el-table-column>
		    <el-table-column prop = "createAt" label = "创建时间" width="180"></el-table-column>
		    <el-table-column label = "操作" width = "150">
		      <template slot-scope = "scope">
		        <el-button size = "mini" type = "primary" :disabled = "!isAuth('materialLink:update')"
                       @click = "advertUpdates(scope.row)">修改</el-button>
		        <el-button size = "mini" type = "danger" :disabled = "!isAuth('materialLink:delete')"
                       @click = "advertdeletes(scope.row)">删除</el-button>
		      </template> 
		    </el-table-column>
		  </el-table>
    <!-- 添加素材 -->
		  <el-dialog title = "添加素材" :visible.sync = "dialogFormVisible" center>
		    <div style = "margin-bottom: 10px;display: flex;">
		      <span style = "width: 200px;display: inline-block;text-align: right;">素材图片：</span>
		      <div style = " width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
		        <el-upload class = "avatar-uploader" v-model = "imgUrl" :action="$http.adornUrl('alioss/upload')" 
                       :show-file-list = "false" :on-success = "handleAvatarSuccess">
		          <img v-if = "imgUrl" :src = "imgUrl" class = "avatar" style = "border-radius: 6px;width:148px;height: 148px;"/>
		          <i v-else class = "el-icon-plus avatar-uploader-icon"></i>
		        </el-upload>
		      </div>
		    </div>
		    <div style = "margin-bottom: 10px;">
		      <span style = "width: 200px;display: inline-block;text-align: right;position: relative;top: -68px;">素材地址：</span>
		      <el-input style = "width:50%;" v-model = "linkUrl" placeholder = "请输入素材地址" type = "textarea" :rows = "4"></el-input>
		    </div>
		    <div slot = "footer" class = "dialog-footer">
		      <el-button @click = "dialogFormVisible = false">取 消</el-button>
		      <el-button type = "primary" @click = "advertNoticeTo()">确 定</el-button>
		    </div>
		</el-dialog>
    <!-- 修改素材 -->
		<el-dialog title = "修改素材" :visible.sync = "dialogFormVisible1" center>
			<el-form :model = "form">
				<el-form-item label = "素材图片：" :label-width = "formLabelWidth">
				 <div style = " width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height:148px;">
					<el-upload class = "avatar-uploader" v-model = "form.imgUrl" :action="$http.adornUrl('alioss/upload')" 
                     :show-file-list = "false" :on-success = "handleAvatarSuccess1">
						<img v-if = "form.imgUrl" :src = "form.imgUrl" class = "avatar" style = "border-radius: 6px;width:148px;height: 148px;"/>
						<i v-else class = "el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				 </div>
				</el-form-item>
			  <el-form-item label = "素材链接：" :label-width = "formLabelWidth">
			    <el-input v-model = "form.linkUrl" style = "width:65%;" type = "textarea" :rows = "4"></el-input>
			  </el-form-item>
			</el-form>
		  <div slot = "footer" class = "dialog-footer">
		    <el-button @click = "dialogFormVisible1 = false">取 消</el-button>
		    <el-button type = "primary" @click = "amendadvertTo()">确 定</el-button>
		  </div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
    data () {
      return {
        imgUrl: '',
        linkUrl: '',
        type: 3,
        form: {
          id: '',
          imgUrl: '',
          linkUrl: '',
          createAt: ''
        },
        formLabelWidth: '200px',
        tableDataLoading: false,
        dialogFormVisible1: false,
        dialogFormVisible: false,
        guangaoData: [],
      }
    },
    methods: {
      handleAvatarSuccess (file) {
        this.imgUrl = file.data
        this.linkUrl = this.imgUrl
      },
      handleAvatarSuccess1 (file) {
        this.form.imgUrl = file.data
        this.form.linkUrl = this.form.imgUrl
      },
      handleSizeChange (val) {
        this.size = val
        this.guangaoSelect()
      },
      handleCurrentChange (val) {
        this.page = val
        this.guangaoSelect()
      },
      // 刷新
      refresh () {
        this.choicenSelect()
        this.handpick()
      },
      // 添加素材弹框
      advertNotice () {
        this.dialogFormVisible = true
      },
      // 添加素材
      advertNoticeTo () {
        if (this.linkUrl == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入跳转链接',
            type: 'warning'
          })
          return
        }
        if (this.imgUrl == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传素材图',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl(`banner/update/-1?url=${this.linkUrl}&image=${this.imgUrl}&type=${this.type}`),
          method: 'post',
          data: this.$http.adornData({})
        }).then(({data}) => {
          this.dialogFormVisible = false
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.imgUrl = ''
              this.linkUrl = ''
              this.guangaoSelect()
            }
          })
        })
      },
      // 点击修改
      advertUpdates (rows) {
        this.form.id = rows.id
        this.form.imgUrl = rows.image_url
        this.form.linkUrl = rows.url
        this.dialogFormVisible1 = true
      },
      // 确定修改
      amendadvertTo () {
        if (this.form.linkUrl == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入跳转链接',
            type: 'warning'
          })
          return
        }
        if (this.form.imgUrl == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传素材图',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl(`banner/update/${this.form.id}?url=${this.form.linkUrl}&image=${this.form.imgUrl}&type=${this.type}`),
          method: 'post',
          data: this.$http.adornData({})
        }).then(({data}) => {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.guangaoSelect()
              this.dialogFormVisible1 = false
            }
          })
        })
      },
      // 删除广告
      advertdeletes (row) {
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`banner/delete/${row.id}`),
            method: 'get',
            params: this.$http.adornParams({})
          }).then(({data}) => {
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.guangaoSelect()
              }
            })
          })
        })
      },
      // 获取素材数据
      guangaoSelect () {
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('banner/user/list/3'),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({data}) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.guangaoData = returnData
        })
      },
    },
    mounted () {
      this.guangaoSelect()
    }
  }
</script>

<style>
	.customWidth {
    width: 80% !important;
  }
</style>
