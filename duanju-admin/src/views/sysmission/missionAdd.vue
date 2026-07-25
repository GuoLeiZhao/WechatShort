<template>
  <div class="components-container main-cont">
    <div style="position: relative;">
		<span>任务标题：</span>
		<el-input style="width: 200px;" placeholder="请输入任务标题" v-model="title" autosize></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
		<span>任务分类：</span>
		<el-select v-model="classifyId" placeholder="请选择任务分类" style="width:200px;">
			<el-option v-for="(item,index) in artiCletypes" :key="item.index" :label="item.classifyName" :value="item.id">
			</el-option>
		</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
		<span>幸运值：</span>
		<el-input style="width:200px;" placeholder="请输入幸运值" type="number" v-model="luckyValue" autosize></el-input>&nbsp;&nbsp;
		<div style="display: flex;align-items: center;margin: 2% 0;">
			<span style="margin-right: 5px;">标题图片：</span>
			<el-upload
				v-model="titlePicture"
				:class="{hide:hideUpload}"
				  :action="$http.adornUrl('alioss/upload')" 
				  :limit=1
				  :on-success="handleUploadSuccess"
				  list-type="picture-card"
				  :on-remove="handleRemove"
				  :on-change="handleChange"
				>
				<i class="el-icon-plus"></i>
			</el-upload>
		</div>
		<div style="position: absolute;right:0;top:5px;">
			<el-button style='margin:0 0 20px 20px;' size="mini" type="primary" icon="document" @click="artiReleass">发布任务
			</el-button>
		</div>
    </div>
    <div class="tinymce-content">
      <textarea id="tinymceId" :value="content" class="tinymce-textarea"/>
    </div>
  </div>
</template>

<script>
    import load from './tinymce-components/dynamicLoadScript'
    import toolbar from './tinymce-components/toolbar'
    import plugins from './tinymce-components/plugins'

    const tinymceCDN = 'https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/tinymce.min.js'

    export default {
        name: 'Tinymce',
        data() {
            return {
                classifyId: '',
                title: '',
                luckyValue: '',
				titlePicture:'',
				isShow: false,
				hideUpload: false,
				limit:1,
                hasChange: false,
                hasInit: false,
                tinymceId: 'tinymceId',
                height: 235,
                toolbar: [],
                menubar: 'file edit insert view format table',
                languageTypeList: {
                    'en': 'en',
                    'zh': 'zh_CN'
                },
				artiCletypes: [],
                // 富文本内容
                content: ''
            }
        },
        watch: {
            content(val) {
                if (!this.hasChange && this.hasInit) {
                    this.$nextTick(() => window.tinymce.get(this.tinymceId).setContent(val || ''))
                }
            }
        },
        methods: {
			handleChange(file, fileList){
				this.hideUpload = fileList.length >= this.limit;
			},
			handleRemove(file, fileList) {
				this.hideUpload = fileList.length >= this.limit;
			},
			//上传成功
			handleUploadSuccess(file,fileList) {
			  this.titlePicture= file.data
			},
			// 添加任务
			artiReleass(){
				if (this.title == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入任务标题',
				        type: 'warning'
				    });
				    return
				}
				if (this.classifyId == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择任务分类',
				        type: 'warning'
				    });
				    return
				}
				if (this.luckyValue == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择任务幸运值',
				        type: 'warning'
				    });
				    return
				}
				if (this.content == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择任务内容',
				        type: 'warning'
				    });
				    return
				}
				this.$http({
				  url: this.$http.adornUrl('helpTaskPlatform/saveHelpTaskPlatform'),
				  method: 'post',
				  data: this.$http.adornData({
				    'title':this.title,
				    'classifyId':this.classifyId,
				    'content':this.content,
				    'luckyValue':this.luckyValue,
				    'titlePicture':this.titlePicture
				  })
				}).then(({data}) => {
					this.dialogFormVisible = false
				    this.$message({
				      message: '添加成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
				        this.$router.push({path: '/missionsye'});
				      }
				    })
				})
			},
            init() {
                load(tinymceCDN, (err) => {
                    if (err) {
                        this.$message.error(err.message)
                        return
                    }
                    this.initTinymce()
                })
            },

            initTinymce() {
                const _this = this
                window.tinymce.init({
                    selector: `#${this.tinymceId}`,
                    images_upload_handler: function (blobInfo, success, failure) {
                        var xhr, formData;

                        xhr = new XMLHttpRequest();
                        xhr.withCredentials = false;
                        xhr.open('POST', "https://your-admin-domain.com/sqx_fast/alioss/upload");
                        xhr.onload = function () {
                            var json;

                            if (xhr.status != 200) {
                                failure('HTTP Error: ' + xhr.status);
                                return;
                            }
                            json =JSON.parse(xhr.responseText);
                            if (json == "" || json == null) {
                                failure('Invalid JSON: ' + xhr.responseText);
                                return;
                            }
                            success(json.data);
                        };
                        formData = new FormData();
                        formData.append('file', blobInfo.blob(), blobInfo.filename());
                        xhr.send(formData);
                    },
                    language: this.languageTypeList['zh'],
                    height: this.height,
                    body_class: 'panel-body ',
                    object_resizing: false,
                    toolbar: this.toolbar.length > 0 ? this.toolbar : toolbar,
                    plugins,
                    menubar: this.menubar,
                    end_container_on_empty_block: true,
                    powerpaste_word_import: 'clean',
                    code_dialog_height: 450,
                    code_dialog_width: 1000,
                    advlist_bullet_styles: 'square',
                    advlist_number_styles: 'default',
                    imagetools_cors_hosts: ['www.tinymce.com', 'codepen.io'],
                    default_link_target: '_blank',
                    link_title: false,
                    // Image
                    imagetools_toolbar: 'rotateleft rotateright | flipv fliph | editimage imageoptions',
                    nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
                    init_instance_callback: editor => {
                        if (_this.content) {
                            editor.setContent(_this.content)
                        }
                        _this.hasInit = true
                        editor.on('NodeChange Change KeyUp SetContent', () => {
                            this.hasChange = true
                            this.content = editor.getContent()
                        })
                    },
                    setup(editor) {
                        editor.on('FullscreenStateChanged', (e) => {
                            _this.fullscreen = e.state
                        })
                    }
                })
            },
            destroyTinymce() {
                const tinymce = window.tinymce.get(this.tinymceId)
                if (this.fullscreen) {
                    tinymce.execCommand('mceFullScreen')
                }
                if (tinymce) {
                    tinymce.destroy()
                }
            },
			// 获取分类
			classifySelect () {
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl('platformClassify/selectClassifyList'),
				method: 'get',
				params: this.$http.adornParams({
				})
			  }).then(({data}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.artiCletypes = returnData
			  })
			},
        },
        mounted() {
            this.init()
			this.classifySelect()
        },
        beforeDestroy() {
            this.destroyTinymce()
        },
        activated() {
            if (window.tinymce) {
                this.initTinymce()
            }
        },
        deactivated() {
            this.destroyTinymce()
        }
    }
</script>

<style>
  .main-cont {
    max-width: 100%;
    min-width: 80%;
	padding-bottom:5%;
	background-color: #fff;
  }

  .tinymce-container {
    position: relative;
    line-height: normal;
  }

  .tinymce-container >>> .mce-fullscreen {
    z-index: 10000;
  }

  .tinymce-textarea {
    visibility: hidden;
    z-index: -1;
  }
 .hide .el-upload--picture-card {
     display: none;
 }
</style>
