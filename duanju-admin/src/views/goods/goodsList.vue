<template>
	<el-tabs v-model = "activeName">
		<el-tab-pane label = "商品列表" name = "first">
			<div style = "margin-right:2%;">
				<span>起止日期：</span>
				<el-date-picker
					v-model="dateList"
					type="datetimerange"
					range-separator="至"
					start-placeholder="开始日期"
					end-placeholder="结束日期"
					format="yyyy-MM-DD HH:mm:ss"
					value-format="yyyy-MM-DD HH:mm:ss"
					@change="dateChangeSelect"
				>
				</el-date-picker>&nbsp;&nbsp;&nbsp;&nbsp;
			  <div style="position: relative;display: inline-block;">
			  	<span>商品编号：</span>
			  	<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入商品编号"
			  		v-model="goodsId"></el-input>&nbsp;&nbsp;
			  </div>
			  <div style="position: relative;display: inline-block;">
			  	<span>商品名称：</span>
			  	<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入商品名称"
			  		v-model="goodsNameLike"></el-input>&nbsp;&nbsp;
			  </div>
			  <div style="position: relative;display: inline-block;">
			  	<span>商品显隐：</span>
				<el-select v-model="goodsShelves" placeholder="请选择"
					style="width: 200px;"
					@change="select"
				>
					<el-option key='1' label="上架" :value="true"></el-option>
					<el-option key='2' label="下架" :value="false"></el-option>
				</el-select>&nbsp;&nbsp;
			  </div>
			  <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
			  </el-button>
			  <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
			  </el-button>
			  <el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="createDialog">创建
			  </el-button>
			</div>
			<el-table v-loading = "tableDataLoading" :data = "tableData.records" >
			<el-table-column fixed prop = "id" label = "商品ID"></el-table-column>
			<el-table-column prop = "pic" label = "商品banner">
				<template slot-scope="scope">
					<el-popover placement="top-start" title="" trigger="hover">
						<img style="width: 50px;" :src="scope.row.pic" alt="" slot="reference">
						<img style="width: 200px;" :src="scope.row.pic" alt="">
					</el-popover>
				</template>
			</el-table-column>
			<el-table-column prop = "name" label = "商品名称"></el-table-column>
			<el-table-column prop = "price" label = " 商品积分"></el-table-column>
			<el-table-column prop = "stock" label = "商品库存"></el-table-column>
			<el-table-column prop = "sort" label = "排序权重"></el-table-column>
			<!-- <el-table-column prop = "shelves" label = "商品状态">
				<template slot-scope="scope">
					<span v-if="scope.row.shelves">已上架</span>
					<span v-else>下架</span>
				</template>
			</el-table-column> -->
			<el-table-column prop = "createdBy" label = "创建人"></el-table-column>
			<el-table-column prop = "createdAt" label = "创建时间"></el-table-column>
			<el-table-column label = "操作" align="center" fixed="right">
				<template slot-scope = "scope">
					<el-button size = "mini" @click="editDialog(scope.row.id)">编辑</el-button>
					<el-button size = "mini" @click="changeShelves(scope.row)">
						<span v-if="scope.row.shelves">下架</span>
						<span v-else>上架</span>
					</el-button>
					<el-button
						size = "mini"
						type = "danger"
						:disabled = "!isAuth('orderCenter:delete')"
						@click = "deletes(scope.row.id)"
					>删除</el-button>
				</template>
			</el-table-column>
			</el-table>
			<div style = "text-align: center;margin-top: 10px;float:right">
			  	<el-pagination @size-change = "handleSizeChange" @current-change = "handleCurrentChange" :page-sizes = "[10, 20, 30, 40, 100, 200, 500]"
							   :page-size = "limit" :current-page = "page" layout = "total,sizes, prev, pager, next,jumper"
							   :total = "tableData.totalCount">
			  	</el-pagination>
			</div>
			<el-dialog title="创建商品" :visible.sync="showCreateDialog" center width="80%">
				<el-form :model="createGoodsDetail" :rules="createRules" ref="createForm" label-width="100px">
				<el-form-item label="商品名称" prop="name">
					<el-input v-model="createGoodsDetail.name" style="width: 300px" placeholder="请输入商品名称"></el-input>
				</el-form-item>
				<el-form-item label="商品积分" prop="price">
					<el-input-number v-model="createGoodsDetail.price" :min="0" label="请输入商品积分"></el-input-number>
				</el-form-item>
				<el-form-item label="商品库存" prop="stock">
					<el-input-number v-model="createGoodsDetail.stock" :min="0" label="请输入商品库存"></el-input-number>
				</el-form-item>
				<el-form-item label="商品权重" prop="sort">
					<el-input-number v-model="createGoodsDetail.sort" :min="0" label="请输入商品权重"></el-input-number>
				</el-form-item>
				<el-form-item label="商品banner" prop="pic">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="createGoodsDetail.pic"
							:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
							accept="image/png, image/jpeg, image/jpg"
							:on-success="handleAvatarSuccess5">
							<img v-if="createGoodsDetail.pic" :src="createGoodsDetail.pic" class="avatar"
								style="border-radius: 6px;width: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
						</el-upload>
					</div>
				</el-form-item>
				<el-form-item label="商品简介" prop="desc">
					<el-input v-model="createGoodsDetail.desc" style="display: none;"></el-input>
					<quill-editor ref="myTextEditor" v-model="createGoodsDetail.desc" :options="quillOption"
						style="padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
				</el-form-item>
				<!-- <el-form-item>
					<el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
					<el-button @click="resetForm('ruleForm')">重置</el-button>
				</el-form-item> -->
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="closeCreateDialog">取 消</el-button>
					<el-button type="primary" @click="createGoods()">确 定</el-button>
				</div>

			</el-dialog>
			<el-dialog title="编辑商品" :visible.sync="showEditDislog" center width="80%">
				<el-form :model="editGoodsDetail" :rules="createRules" ref="editForm" label-width="100px">
				<el-form-item label="商品名称" prop="name">
					<el-input v-model="editGoodsDetail.name" style="width: 300px" placeholder="请输入商品名称"></el-input>
				</el-form-item>
				<el-form-item label="商品积分" prop="price">
					<el-input-number v-model="editGoodsDetail.price" :min="0" label="请输入商品积分"></el-input-number>
				</el-form-item>
				<el-form-item label="商品库存" prop="stock">
					<el-input-number v-model="editGoodsDetail.stock" :min="0" label="请输入商品库存"></el-input-number>
				</el-form-item>
				<el-form-item label="商品权重" prop="sort">
					<el-input-number v-model="editGoodsDetail.sort" :min="0" label="请输入商品权重"></el-input-number>
				</el-form-item>
				<el-form-item label="商品banner" prop="pic">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="editGoodsDetail.pic"
							:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
							accept="image/png, image/jpeg, image/jpg"
							:on-success="handleAvatarSuccess5">
							<img v-if="editGoodsDetail.pic" :src="editGoodsDetail.pic" class="avatar"
								style="border-radius: 6px;width: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
						</el-upload>
					</div>
				</el-form-item>
				<el-form-item label="商品简介" prop="desc">
					<el-input v-model="editGoodsDetail.desc" style="display: none;"></el-input>
					<quill-editor ref="myTextEditor" v-model="editGoodsDetail.desc" :options="quillOption"
						style="padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
				</el-form-item>
				<!-- <el-form-item>
					<el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
					<el-button @click="resetForm('ruleForm')">重置</el-button>
				</el-form-item> -->
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="closeEditDialog">取 消</el-button>
					<el-button type="primary" @click="editGoods()">确 定</el-button>
				</div>

			</el-dialog>
		</el-tab-pane>
	</el-tabs>
</template>
<script>
	import { clearData } from '../../utils';
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../locality/quill-config.js'
    export default {
		components: {
			quillEditor
		},
		data () {
			return {
				page: 1,
				limit: 10,
				activeName: 'first',
				goodsId: '', // 商品 ID
				goodsNameLike: '', // 商品名称
				goodsShelves: '', // 商品显示隐藏
				createdStart: '',
				createdEnd: '',
				tableDataLoading: true,
				tableData: [],
				dateList: [], // 起止日期
				showCreateDialog: false,
				showEditDislog: false,
				createGoodsDetail: {
					name: '',
					pic: '',
					sort: 0,
					desc: '',
					price: 0,
					stock: 0,
				},
				editGoodsDetail: {
					id: '',
					name: '',
					pic: '',
					sort: 0,
					desc: '',
					price: 0,
					stock: 0,
				},
				createRules: {
					name: [
						{ required: true, message: '请输入商品名称', trigger: 'blur' },
					]
				},
				editRules: {
					name: [
						{ required: true, message: '请输入商品名称', trigger: 'blur' },
					]
				},
				quillOption: quillConfig,
			}
		},
		methods: {
			// 创建弹窗打开
			createDialog () {
                this.showCreateDialog = true;
            },
			closeCreateDialog() {
				this.showCreateDialog = false;
				this.createGoodsDetail = {
					name: '',
					pic: '',
					sort: 0,
					desc: '',
					price: 0,
					stock: 0,
				};
				this.$refs['createForm'].resetFields();
			},
            // 创建
            createGoods () {
                this.$refs['createForm'].validate((valid) => {
					if (valid) {
						this.$http({
							url: this.$http.adornUrl('shop/item'),
							method: 'post',
							data: this.$http.adornParams({
								...this.createGoodsDetail
							})
						}).then(({ data }) => {
							if (data && data.code === 0) {
								// 成功
								this.$message({
									message: '添加成功',
									type: 'success',
									duration: 1000,
									onClose: () => {
										this.showCreateDialog = false;
										this.createGoodsDetail = {
											name: '',
											pic: '',
											sort: 0,
											desc: '',
											price: 0,
											stock: 0,
										};
										this.$refs['createForm'].resetFields();
										this.dataSelect()
									}
								})
							} else {
								this.$message.error(data.msg)
							}
						})
					} else {
						this.$message.error('请校验填写内容');
					}
				})
            },
			editDialog(id) {
				console.log(id, 'id');
				this.$http({
					url: this.$http.adornUrl(`shop/item/selectById/${id}`),
					method: 'get',
				}).then(({ data }) => {
					console.log(data, '_data');
					if (data && data.code === 0) {
						const { id, name, pic, sort, desc, price, stock } = data.data;
						this.editGoodsDetail = {
							id,
                            name,
                            pic,
                            sort,
                            desc,
                            price,
                            stock,
						};
						this.showEditDislog = true;
					}
				})
			},
			closeEditDialog () {
				this.showEditDislog = false;
				this.editGoodsDetail = {
					name: '',
					pic: '',
					sort: 0,
					desc: '',
					price: 0,
					stock: 0,
				};
				this.$refs['editForm'].resetFields();
			},
			editGoods() {
				this.$refs['editForm'].validate((valid) => {
					if (valid) {
						this.$http({
							url: this.$http.adornUrl('shop/item'),
							method: 'put',
							data: this.$http.adornParams({
								...this.editGoodsDetail
							})
						}).then(({ data }) => {
							if (data && data.code === 0) {
								// 成功
								this.$message({
									message: '修改成功',
									type: 'success',
									duration: 1000,
									onClose: () => {
										this.showEditDislog = false;
										this.editGoodsDetail = {
											name: '',
											pic: '',
											sort: 0,
											desc: '',
											price: 0,
											stock: 0,
										};
										this.$refs['editForm'].resetFields();
										this.dataSelect()
									}
								})
							} else {
								this.$message.error(data.msg)
							}
						})
					} else {
						this.$message.error('请校验填写内容');
					}
				})
			},
			// 封面图片上传
			handleAvatarSuccess5(file) {
				this.createGoodsDetail.pic = file.data;
			},
            // 查询
			handleSizeChange (val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange (val) {
				this.page = val
				this.dataSelect()
			},
			// 日期选择时的查询
			dateChangeSelect() {
				console.log(this.dateList, '_val');
				if (this.dateList && this.dateList.length > 0) {
					this.createdStart = this.dateList[0];
					this.createdEnd = this.dateList[1];
				} else {
					this.createdStart = '';
                    this.createdEnd = '';
				}
			},
			// 查询资源列表
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置资源列表
			cleans() {
				this.page = 1
				this.goodsId = ''
				this.goodsNameLike = ''
				this.goodsShelves = ''
				this.dataSelect()
			},
			// 删除banner图
			deletes (id) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`/shop/item/${id}`),
						method: 'delete',
					}).then(({data}) => {
						if (data && data.code === 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1000,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message.error(data.msg);
						}

					})
				}).catch(() => {
				})
			},
			// 修改商品上下架
			changeShelves (row) {
				const { id, shelves } = row;
				this.$http({
					url: this.$http.adornUrl(`/shop/item/shelves/${id}/${!shelves}`),
					method: 'put',
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1000,
                            onClose: () => {
                                this.dataSelect()
                            }
						})
					} else {
						this.$message.error(data.msg);
					}
				})
			},
			// 获取数据列表
			dataSelect () {
				this.tableDataLoading = true
				this.totalMoney = 0
				this.$http({
					url: this.$http.adornUrl('shop/item/selectPage'),
					method: 'get',
					params: this.$http.adornParams(clearData({
						'currPage': this.page,
						'pageSize': this.limit,
						'id': this.goodsId,
						'nameLike': this.goodsNameLike,
						'shelves': this.goodsShelves,
                        'createdStart': this.createdStart,
                        'createdEnd': this.createdEnd,
					}))
				}).then(({data}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						// for (var i in data.data.list) {
						// 	if (data.data.list[i].payMoney) {
						// 		this.totalMoney = this.totalMoney + Number(data.data.list[i].payMoney)
						// 	}
						// }
						let returnData = data.data
						this.tableData = returnData
					}
				})
			},
			// 详情跳转
			updates (row) {
				this.$router.push({path: '/userDetail', query: {userId: row.userId}})
			},
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
