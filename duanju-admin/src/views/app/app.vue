<template>
	<div>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<div style="float: right;margin-right:2%;">
			<el-button style='margin: 10px 0;' :disabled="!isAuth('app:add')" size="mini" type="primary"
				icon="document" @click="addUpgrade">添加升级</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="upgradeData1.records">
			<el-table-column fixed prop="id" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="wgtUrl" label="统一地址">
			</el-table-column>
			<el-table-column prop="androidWgtUrl" label="安卓">
			</el-table-column>
			<el-table-column prop="iosWgtUrl" label="苹果">
			</el-table-column>
			<el-table-column prop="version" label="安卓版本">
			</el-table-column>
			<el-table-column prop="iosVersion" label="ios版本">
			</el-table-column>
			<el-table-column prop="content" label="升级方式">
				<template slot-scope="scope">
					<span>{{scope.row.method == 'true' ? '强制升级' : '普通升级'}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="createAt" label="创建时间">
			</el-table-column>
			<el-table-column label="操作" width="200">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="!isAuth('app:update')"
						@click="upgradebj(scope.$index, scope.row)">编辑
					</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('app:delete')"
						@click="upgradelete(scope.row)">删除
					</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
				:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="upgradeData1.total">
			</el-pagination>
		</div>
		<!-- 添加升级弹框 -->
		<el-dialog title="添加升级" :visible.sync="dialogFormVisible3" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>统一地址：</span>
				<el-input style="width: 50%;" v-model="wgtUrl" placeholder="请输入统一地址"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>安卓地址：</span>
				<el-input style="width:50%;" v-model="androidWgtUrl" placeholder="请输入安卓地址"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>苹果地址：</span>
				<el-input style="width: 50%;" v-model="iosWgtUrl" placeholder="请输入苹果地址"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>安卓版本号：</span>
				<el-input style="width: 50%;" v-model="version" placeholder="请输入安卓版本号"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>ios版本号：</span>
				<el-input style="width: 50%;" v-model="iosVersion" placeholder="请输入ios版本号"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>升级方式：</span>
				<el-select v-model="method" placeholder="请选择升级方式" style="width:50%;">
					<el-option v-for="item in methods" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;"><i
						style="color: #f56c6c;margin-right: 2px;">*</i>升级内容：</span>
				<el-input style="width: 50%;" v-model="des" placeholder="请输入升级内容"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible3 = false">取 消</el-button>
				<el-button type="primary" @click="upgradaddTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改升级弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible4" center>
			<el-form :model="formupgrad">
				<el-form-item label="统一地址：" :label-width="formLabelWidth">
					<el-input v-model="formupgrad.wgtUrl" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="安卓地址：" :label-width="formLabelWidth">
					<el-input v-model="formupgrad.androidWgtUrl" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="苹果地址：" :label-width="formLabelWidth">
					<el-input v-model="formupgrad.iosWgtUrl" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="安卓版本号：" :label-width="formLabelWidth">
					<el-input v-model="formupgrad.version" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="ios版本号：" :label-width="formLabelWidth">
					<el-input v-model="formupgrad.iosVersion" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="升级方式：" :label-width="formLabelWidth">
					<el-select v-model="formupgrad.method" placeholder="请选择升级方式" style="width:65%;">
						<el-option v-for="item in methods" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="升级内容：" :label-width="formLabelWidth">
					<el-input v-model="formupgrad.des" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible4 = false">取 消</el-button>
				<el-button type="primary" @click="upgradbjTo()">确 定</el-button>
			</div>
		</el-dialog>

	</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				state: 1,
				url: '',
				title: '',
				type: '',
				id: '',
				nav: '',
				keyword: '',
				image_url: '',
				androidWgtUrl: '',
				iosWgtUrl: '',
				wgtUrl: '',
				version: '',
				iosVersion: '',
				des: '',
				checkBoxData: [], //多选框选择的值
				method: 'false',
				methods: [{
						value: 'false',
						label: '普通升级'
					},
					{
						value: 'true',
						label: '强制升级'
					}
				],
				formupgrad: {
					id: '',
					androidWgtUrl: '',
					iosWgtUrl: '',
					wgtUrl: '',
					version: '',
					des: '',
					method: '',
					iosVersion: ''
				},
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: true,
				dialogFormVisible3: false,
				dialogFormVisible4:false,
				tableData: [],
				homeData: [],
				choicenData2: [],
				upgradeData: [],
				upgradeData1: [],
				choicenData: [],
				types: [{
						value: 'word',
						label: '文字'
					},
					{
						value: 'url',
						label: '链接'
					}
				],
				bannerData: [],
				form1: {
					id: '',
					url: '',
					image_url: ''
				},
				form: {
					id: '',
					title: '',
					url: '',
					type: '',
					state: '',
					types: [{
							value: 'word',
							label: '文字'
						},
						{
							value: 'url',
							label: '链接'
						}
					],
				},
				formcomp: {
					id: '',
					state: '',
					title: '',
					url: '',
					image_url: ''
				}
			}
		},
		methods: {

			handleSizeChange2(val) {
				this.limit = val;
				this.upgradeSelect()
			},
			handleCurrentChange2(val) {
				this.page = val;
				this.upgradeSelect()
			},

			handleClick(tab, event) {
				this.upgradeSelect()
			},
			// 添加升级弹框
			addUpgrade() {
				this.dialogFormVisible3 = true
			},
			// 添加升级
			upgradaddTo() {
				if (this.wgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入统一地址',
						type: 'warning'
					});
					return
				}
				if (this.androidWgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入安卓地址',
						type: 'warning'
					});
					return
				}
				if (this.iosWgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入苹果地址',
						type: 'warning'
					});
					return
				}

				if (this.version == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入安卓版本号',
						type: 'warning'
					});
					return
				}
				if (this.iosVersion == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入苹果版本号',
						type: 'warning'
					});
					return
				}

				if (this.method == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择升级方式',
						type: 'warning'
					});
					return
				}
				if (this.des == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入升级内容',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('appinfo/save'),
					method: 'post',
					data: this.$http.adornData({
						'androidWgtUrl': this.androidWgtUrl,
						'iosWgtUrl': this.iosWgtUrl,
						'wgtUrl': this.wgtUrl,
						'version': this.version,
						'iosVersion': this.iosVersion,
						'method': this.method,
						'des': this.des
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible3 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.upgradeSelect()
						}
					})
				})
			},
			// 修改升级弹框
			upgradebj(index, rows) {
				this.dialogFormVisible4 = true;
				this.formupgrad.androidWgtUrl = rows.androidWgtUrl;
				this.formupgrad.id = rows.id;
				this.formupgrad.iosWgtUrl = rows.iosWgtUrl;
				this.formupgrad.wgtUrl = rows.wgtUrl;
				this.formupgrad.version = rows.version;
				this.formupgrad.iosVersion = rows.iosVersion;
				this.formupgrad.des = rows.des;
				this.formupgrad.method = rows.method;
			},
			// 修改升级
			upgradbjTo() {
				this.$http({
					url: this.$http.adornUrl('appinfo/save'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.formupgrad.id,
						'androidWgtUrl': this.formupgrad.androidWgtUrl,
						'iosWgtUrl': this.formupgrad.iosWgtUrl,
						'wgtUrl': this.formupgrad.wgtUrl,
						'version': this.formupgrad.version,
						'iosVersion': this.formupgrad.iosVersion,
						'des': this.formupgrad.des,
						'method': this.formupgrad.method,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible4 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.upgradeSelect()
						}
					})
				})
			},
			// 删除升级
			upgradelete(row) {
				let delid = row.id
				this.id = delid
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`appinfo/delete/${this.id}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = '';
								this.upgradeSelect()
							}
						})
					})
				}).catch(() => {})
			},

			// 升级配置数据列表
			upgradeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`appinfo/list?page=${this.page}&limit=${this.limit}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.upgradeData1 = returnData
				})
			},

		},
		mounted() {
			console.log('···························')
			this.upgradeSelect()
		}
	};
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
</style>
