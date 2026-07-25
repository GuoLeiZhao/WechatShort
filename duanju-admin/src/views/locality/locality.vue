<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="社区分类" name="first">
				<div style="float: right;margin-right:2%;">
					<el-button style="margin: 10px 0;" :disabled="!isAuth('locality:add')" size="mini" type="primary"
						icon="document" @click="addNavigation">添加分类</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="homeData">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="title" label="名称">
					</el-table-column>
					<el-table-column prop="url" label="发布类型">
						<template slot-scope="scope">
							<span v-if="scope.row.status==1">详情</span>
							<span v-if="scope.row.status==2">列表</span>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="图片">
						<template slot-scope="scope">
							<img :src="scope.row.imageUrl" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="160">
					</el-table-column>
					<el-table-column prop="state" label="是否启用">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.state" @change="change2(scope.row.state,scope.row)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" v-if="scope.row.status === 0 "
								@click="shenhe( scope.row)">审核</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('locality:update')"
								@click="compile(scope.$index, scope.row)">编辑
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
								@click="navdeletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<!-- 添加分类弹框 -->
				<el-dialog title="添加分类" :visible.sync="dialogFormVisible9" center>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
						<el-input style="width:50%;" v-model="title" placeholder="请输入分类标题"></el-input>
					</div>
					<div style="display: flex;">
						<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
						<div
							style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
							<el-upload class="avatar-uploader" v-model="imageUrl"
								:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
								:on-success="handleAvatarSuccess4">
								<img v-if="imageUrl" :src="imageUrl" class="avatar"
									style="border-radius: 6px;width: 86px;height: 86px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</div>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">发布类型：</span>
						<el-radio-group v-model="statusIdd">
							<el-radio :label="1">详情</el-radio>
							<el-radio :label="2">列表</el-radio>
						</el-radio-group>
					</div>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible9 = false">取 消</el-button>
						<el-button type="primary" @click="NagNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
			</el-tab-pane>
			<el-tab-pane label="社区轮播" name="second">
				<div style="float: right;margin-right:2%;">
					<el-button style="margin: 10px 0;" :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
						icon="document" @click="addNoticelun">添加轮播图</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="bannerData" height="450px">
					<el-table-column fixed prop="id" label="编号" width="50"></el-table-column>
					<el-table-column prop="imageUrl" label="头像">
						<template slot-scope="scope">
							　　<img :src="scope.row.imageUrl" width="60" height="60" />
						</template>
					</el-table-column>
					<el-table-column prop="name" label="轮播图名称"></el-table-column>
					<el-table-column prop="state" label="状态">
						<template slot-scope="scope">
							<span v-if="scope.row.state == 1">显示</span>
							<span v-if="scope.row.state == 2" style="color: #f56c6c;">隐藏</span>
						</template>
					</el-table-column>
					<el-table-column prop="state" label="是否启用">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
								@click="amendBanner(scope.$index, scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
								@click="deletes(scope.row)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;float:right">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="bannerData.length">
					</el-pagination>
				</div>
				<!-- 添加弹框 -->
				<el-dialog title="添加轮播图" :visible.sync="dialogFormVisible6" center>
					<div style="margin-bottom: 10px;display: flex;">
						<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
						<div
							style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
							<el-upload class="avatar-uploader" v-model="imageUrl"
								:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
								:on-success="handleAvatarSuccess">
								<img v-if="imageUrl" :src="imageUrl" class="avatar"
									style="border-radius: 6px;width: 148px;height: 148px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</div>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
						<el-input style="width:50%;" v-model="url" placeholder="请输入链接"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
						<el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
						<el-input style="width:50%;" v-model="describes" placeholder="请输入描述"></el-input>
					</div>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible6= false">取 消</el-button>
						<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
				<!-- 修改弹框 -->
				<el-dialog title="修改轮播图" :visible.sync="dialogFormVisible1" center>
					<div style="margin-bottom: 10px;display: flex;">
						<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
						<div
							style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
							<el-upload class="avatar-uploader" v-model="imageUrl"
								:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
								:on-success="handleAvatarSuccess">
								<img v-if="imageUrl" :src="imageUrl" class="avatar"
									style="border-radius: 6px;width: 148px;height: 148px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</div>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
						<el-input style="width:50%;" v-model="url" placeholder="请输入链接"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
						<el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
						<el-input style="width:50%;" v-model="describes" placeholder="请输入描述"></el-input>
					</div>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible1 = false">取 消</el-button>
						<el-button typ="primary" @click="amendNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
			</el-tab-pane>
			<el-tab-pane label="信息列表" name="third">
				<div style="display: inline-block;">
					<span>社区筛选：</span>
					<el-select v-model="campus" style="width:150px;margin-left: 10px;" @change="animeDat2(campus)">
						<el-option v-for="item in homeData2.list" :key="item.campusId" :label="item.campusName"
							:value="item.campusId">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
					<span>分类：</span>
					<el-select v-model="classifyIds" style="width:150px;margin-left: 10px;" @change="animeDat2(status)">
						<el-option v-for="item in homeData1" :key="item.value" :label="item.title" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
					<span>状态：</span>
					<el-select v-model="statusId" style="width:150px;margin-left: 10px;" @change="animeDat3(status)">
						<el-option v-for="item in statuss" :key="item.value" :label="item.title" :value="item.id">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
					<div style="position: relative;display: inline-block;">
						<span>用户手机号:</span>
						<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入手机号"
							v-model="myPhone">
						</el-input>&nbsp;&nbsp;
					</div>
					<div style="position: relative;display: inline-block;">
						<span>标题名称：</span>
						<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入标题名称"
							v-model="search">
						</el-input>&nbsp;&nbsp;
					</div>
					<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="phoneSelect">查询
					</el-button>
					<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="cleans2">重置
					</el-button>&nbsp;&nbsp;
					<el-button style="margin: 10px 0;" :disabled="!isAuth('locality:add')" size="mini" type="primary"
						icon="document" @click="choiaddNotice()">添加信息</el-button>&nbsp;&nbsp;&nbsp;&nbsp;
					<el-button style="margin: 10px 0;"
						:disabled="!isAuth('locality:delete') || multipleSelection.length <= 0 " size="mini"
						type="danger" icon="document" @click="choideletes()">批量删除</el-button>
				</div>
				<div style="float: right;margin-right:2%;">

				</div>
				<el-table v-loading="tableDataLoading" :data="choicenData.records"
					@selection-change="handleSelectionChange">
					<el-table-column type="selection">
					</el-table-column>
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="titleImg" label="封面图">
						<template slot-scope="scope">
							　　<img :src="scope.row.titleImg" width="40" height="40" />
						</template>
					</el-table-column>
					<el-table-column prop="name" label="名称" width="150">
					</el-table-column>
					<el-table-column prop="house" label="户型" width="100">
					</el-table-column>
					<el-table-column prop="area" label="大小" width="100">
					</el-table-column>
					<el-table-column prop="price" label="价格" width="100">
					</el-table-column>
					<el-table-column prop="address" label="地址" width="200">
					</el-table-column>
					<el-table-column prop="remark" label="详情" width="200">
					</el-table-column>
					<el-table-column prop="titleImg" label="详情图" width="180">
						<template slot-scope="scope">
							　　<img v-for="(item,index) in scope.row.imgs" :key="index" :src="item" width="40"
								height="40" @click="refund(scope.row)" />
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="联系电话" width="140">
					</el-table-column>
					<el-table-column prop="contactNum" label="联系次数" width="100">
					</el-table-column>
					<el-table-column prop="classifyName" label="分类" width="100">
					</el-table-column>
					<el-table-column prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span style="color: #4f9dec;" v-if="scope.row.status === 0 ">待审核</span>
							<el-switch v-if="scope.row.status ==1 || scope.row.status ==2" v-model="scope.row.status"
								@change="change3(scope.row.state,scope.row)" :active-value="openValue"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
							<span style="color: #4f9dec;" v-if="scope.row.status === 3 ">驳回</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="240" fixed='right' align="center">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" v-if="scope.row.status === 0 "
								@click="shenhe( scope.row)">审核</el-button>
							<el-button size="mini" type="primary" @click="choiCompile(scope.$index, scope.row)">修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
								@click="choidelete(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;float:right">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="choicenData.total">
					</el-pagination>
				</div>
				<!-- 添加信息弹框 -->
				<el-dialog title="添加信息" customClass="customWidth" :visible.sync="dialogFormVisible5" center>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">分类：</span>
						<el-select v-model="classifyId" style="width:200px;" @change="onChang">
							<el-option v-for="(item,index) in homeData" :key="index" :label="item.title"
								:value="item.id">
							</el-option>
						</el-select>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">社区：</span>
						<el-select v-model="campus" style="width:200px;" @change="onChang3">
							<el-option v-for="(item,index) in homeData2.list" :key="index" :label="item.campusName"
								:value="item.campusId">
							</el-option>
						</el-select>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
						<el-input v-model="name" style="width:200px;" placeholder="请输入标题"></el-input>
					</div>
					<div style="margin-bottom: 10px;display:flex;">
						<span style="width: 200px;display: inline-block;text-align: right;">封面图：</span>
						<div
							style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
							<el-upload class="avatar-uploader" v-model="titleImg"
								:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
								:on-success="handleAvatarSuccess5">
								<img v-if="titleImg" :src="titleImg" class="avatar"
									style="border-radius: 6px;width: 148px;height: 148px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
							</el-upload>
						</div>
					</div>
					<div style="margin-bottom: 10px;display:flex;" v-if="classifyId!=6 &&classifyId!=7">
						<span style="width: 200px;display: inline-block;text-align: right;">详情图：</span>
						<div class="imgs" v-for="(item,index) in img" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="dels(index)"></i>
							</span>
						</div>
						<div class="imgs" style="width: 50%;">
							<el-upload :action="$http.adornUrl('alioss/upload')"  list-type="picture-card"
								:show-file-list="false" :on-success="handleRemove">
								<i class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">联系电话：</span>
						<el-input v-model="phone" style="width:200px;" placeholder="请输入手机号"></el-input>
					</div>
					<div style="margin-bottom: 10px;" v-if="classifyId==5">
						<span style="width: 200px;display: inline-block;text-align: right;">户型：</span>
						<el-input v-model="house" style="width:200px;" placeholder="请输入户型"></el-input>
					</div>
					<div style="margin-bottom: 10px;" v-if="classifyId==5">
						<span style="width: 200px;display: inline-block;text-align: right;">大小：</span>
						<el-input v-model="area" style="width:200px;" placeholder="请输入面积大小"></el-input>
					</div>
					<div style="margin-bottom: 10px;" v-if="classifyId!=6&&classifyId!=7">
						<span style="width: 200px;display: inline-block;text-align: right;">价格：</span>
						<el-input v-model="price" style="width:200px;" placeholder="请输入价格"></el-input>
					</div>
					<div style="margin-bottom: 10px;" v-if="classifyId!=7">
						<span style="width: 200px;display: inline-block;text-align: right;">描述：</span>
						<el-input v-model="remark" style="width:200px;display: none;" placeholder="请输入描述"></el-input>
						<quill-editor ref="myTextEditor" v-model="remark" :options="quillOption"
							style="padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
						</quill-editor>
					</div>
					<div style="margin-bottom: 10px;" v-if="classifyId!=6&&classifyId!=7">
						<span style="width: 200px;display: inline-block;text-align: right;">标签：</span>
						<span class="bqList" v-for="(item,index) in labels" :key="index">{{item}}
							<span class="delss">
								<i class="el-icon-delete" @click="dels1(index)"></i>
							</span>
						</span>
						<el-input v-model="bq" style="width:200px;" placeholder="请输入标签"
							onkeyup="this.value=this.value.replace(/[, ]/g,'')"></el-input>
						<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" circle @click="btnTj">
						</el-button>

					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">地址：</span>
						<el-input v-model="address" @keydown.enter.native="select" style="width:45%;"
							placeholder="请输入详细地址"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">维度：</span>
						<el-input v-model="latitude" style="width:45%;" placeholder="请输入维度"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">经度：</span>
						<el-input v-model="longitude" style="width:45%;" placeholder="请输入经度"></el-input>
					</div>
					<div>
						<div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
					</div>
					<div slot="footer" class="dialog-footer" style="margin-top: 30px;text-align: center;">
						<el-button @click="prev">取 消</el-button>
						<el-button type="primary" @click="addmissionNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
			</el-tab-pane>
			<!-- 社区编辑 -->
			<el-dialog title="修改" :visible.sync="dialogFormVisible2" center>
				<el-form :model="formcomp">
					<el-form-item label="标题：" :label-width="formLabelWidth">
						<el-input v-model="formcomp.title" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="图片：" :label-width="formLabelWidth">
						<div
							style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
							<el-upload class="avatar-uploader" v-model="imageUrl"
								:action="$http.adornUrl('alioss/upload')"  :show-file-list="false"
								:on-success="handleAvatarSuccess3">
								<img v-if="formcomp.imageUrl" :src="formcomp.imageUrl" class="avatar"
									style="border-radius: 6px;width: 86px;height: 86px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</div>
					</el-form-item>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">发布类型：</span>
						<el-radio-group v-model="statusIdd">
							<el-radio :label="1">详情</el-radio>
							<el-radio :label="2">列表</el-radio>
						</el-radio-group>
					</div>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="compileNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 用户列表 -->
			<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center>
				<div style="margin:2% 0;display: inline-block;">
					<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
					<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
						v-model="nickName"></el-input>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
						查询
					</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
						重置
					</el-button>
				</div>
				<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.list">
					<el-table-column fixed prop="nickName" width="220" label="昵称">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.nickName ? scope.row.nickName : '未设置'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" width="220" label="手机号">
						<template slot-scope="scope">
							<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" width="220" label="创建时间">
					</el-table-column>
					</el-table-column>
					<el-table-column label="操作" fixed='right' width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
						:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="userIds.totalElements">
					</el-pagination>
				</div>
			</el-dialog>
			<!--    图片展示-->
			<el-dialog title="图片" :visible.sync="dialogVisible" width="800px">
				<span v-if="imageUrl.length>0" v-for="(item, index) in imageUrl" :key="index" style="">
					<img :src="item" alt="" style="width: 45%; height: 100%;margin: 15px;display:inline-block;">
				</span>
				<span v-else>暂无图片</span>
			</el-dialog>
			<!-- 任务审核弹框 -->
			<el-dialog title="任务审核" :visible.sync="dialogFormVisible8" center>
				<el-radio-group v-model="radio" style="width: 100%;margin-bottom: 30px;margin-left: 200px;">
					<el-radio :label="1">通过</el-radio>
					<el-radio :label="2">驳回</el-radio>
				</el-radio-group>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">驳回理由：</span>
					<el-input style="width:50%;" v-model="contents" type="text" placeholder="请输入驳回理由"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible8 = false">取 消</el-button>
					<el-button type="primary" @click="refuseto()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from './quill-config.js'
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
				openValue: 1,
				closeValue: 2,
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
				dialogFormVisible8: false,
				dialogFormVisible9: false,
				homeData: [],
				homeData1: [{
					id: 0,
					title: '全部'
				}],
				choicenData2: [],
				choicenData: [],
				url: '',
				imageUrl: '',
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
				statusId: -1,
				statuss: [{
						id: -1,
						title: '全部'
					},
					{
						id: 0,
						title: '待审核'
					},
					{
						id: 1,
						title: '上架'
					},
					{
						id: 2,
						title: '下架'
					},
					{
						id: 3,
						title: '驳回'
					}
				],
				// 房屋信息
				phone: '', //电话
				name: '', //标题
				userIdss: '', //用户id
				titleImg: '', //封面图
				img: [], //详情图
				status: '', //状态0待审核  1上架  2下架 3驳回 (修改添加不传)
				classifyId: 4, //分类id
				area: '', //大小
				house: '', //户型
				price: '', //价格
				labels: [], //标签集合
				address: '', //地址
				latitude: '', //纬度
				longitude: '', //经度
				remark: '', //描述
				options: regionData,
				storeAddress: [],
				storeAddre: '请选择城市',
				province: '', //省
				city: '', //市
				district: '', //区
				ids: '', //信息id
				nickName: '',
				userIds: {}, //用户列表
				bq: '', //输入的标签名
				missions: 0,
				shenheId: 0, //审核id
				contents: '', //驳回理由
				dialogVisible: false,
				radio: 1,
				multipleSelection: [],
				campus: '',
				homeData2: [],
				quillOption: quillConfig,
				statusIdd: 1
			}
		},
		methods: {
			//查看照片
			refund(e) {
				console.log(e)
				this.imageUrl = []
				if (e.img != null) {
					let img = e.img.split(',')
					if (img.length != 0) {
						this.imageUrl = img
					}
				}
				this.dialogVisible = true
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			handleAvatarSuccess3(file) {
				this.formcomp.imageUrl = file.data
			},
			handleAvatarSuccess4(file) {
				this.imageUrl = file.data
			},
			handleSizeChange(val) {
				this.size = val
				this.bannerSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.bannerSelect()
			},
			handleSizeChange1(val) {
				this.limit = val
				this.InformationSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.InformationSelect()
			},
			handleSizeChange2(val) {
				this.size = val
				this.tableDataLoading4 = true
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page = val
				this.tableDataLoading4 = true
				this.userClass()
			},
			handleClick(tab, event) {
				this.campus = ''
				if (tab._props.label == '社区分类') {
					this.nav = 1
					this.homeSelect()
				}
				if (tab._props.label == '社区轮播') {
					this.type = 1
					this.bannerSelect()
				}
				if (tab._props.label == '信息列表') {
					this.type = 2
					this.InformationSelect()
				}
			},
			handleAvatarSuccess(file) {
				this.imageUrl = file.data
			},
			handleAvatarSuccess2(file2) {
				this.form1.imageUrl = file2.data
			},
			// 添加社区分类弹框
			addNavigation() {
				this.dialogFormVisible9 = true
			},
			// 添加社区分类
			NagNoticeTo() {
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					})
					return
				}
				// if (this.url == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入路由',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.imageUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('activity/insertActivity'),
					method: 'post',
					data: this.$http.adornData({
						'state': this.state,
						'url': this.url,
						'title': this.title,
						'imageUrl': this.imageUrl,
						'status': this.statusIdd
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible9 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.url = ''
							this.title = ''
							this.imageUrl = ''
							this.homeSelect()
						}
					})
				})
			},
			// 删除分类
			navdeletes(row) {
				if (row.id > 13) {
					this.$confirm(`确定删除此条信息?`, '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$http({
							url: this.$http.adornUrl(`/activity/delete/${row.id}`),
							method: 'post',
							data: this.$http.adornData({})
						}).then(({
							data
						}) => {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.homeSelect()
								}
							})
						})
					}).catch(() => {})
				} else {
					this.$message({
						message: '此条信息不可删除',
						type: 'warning',
						duration: 1500,
						onClose: () => {}
					})
				}

			},
			// 添加
			addNoticelun() {
				this.dialogFormVisible6 = true
			},
			//轮播数据
			bannerSelect() {
				this.$http({
					url: this.$http.adornUrl('banner/selectBannerList'),
					method: 'get',
					params: this.$http.adornParams({
						'state': this.state,
						'classify': this.classify,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.bannerData = returnData
				})
			},
			// 添加轮播图
			addNoticeTo() {
				if (this.imageUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					})
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('banner/insertBanner'),
					method: 'post',
					data: this.$http.adornData({
						'classify': this.classify,
						'imageUrl': this.imageUrl,
						'state': this.state,
						'url': this.url,
						'name': this.name,
						'describes': this.describes
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible6 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.imageUrl = ''
							this.state = ''
							this.url = ''
							this.name = ''
							this.describes = ''
							this.bannerSelect()
						}
					})
				})
			},
			// 修改轮播图
			amendBanner(index, rows) {
				this.dialogFormVisible1 = true
				this.id = rows.id
				this.imageUrl = rows.imageUrl
				this.url = rows.url
				this.sort = rows.sort
				this.state = rows.state
				this.name = rows.name
				this.describes = rows.describes
			},
			// 确认修改轮播图你
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl(
						`banner/updateBannerById?id=${this.id}&imageUrl=${this.imageUrl}&url=${this.url}&state=${this.state}&sort=${this.sort}&name=${this.name}&describes=${this.describes}`
					),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible1 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.bannerSelect()
						}
					})
				})
			},
			// 删除banner图
			deletes(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`banner/deleteBannerById?id=${delid}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.bannerSelect()
							}
						})
					})
				}).catch(() => {})

			},
			// 修改弹框
			compile(index, rows) {
				this.dialogFormVisible2 = true
				this.formcomp.id = rows.id
				this.formcomp.state = rows.state
				this.formcomp.title = rows.title
				this.formcomp.url = rows.url
				this.formcomp.imageUrl = rows.imageUrl
				this.statusIdd = rows.status
			},
			// 修改商品类别
			compileNoticeTo() {
				if (this.formcomp.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					})
					return
				}
				// if (this.formcomp.url == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入路由',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.formcomp.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl(`/activity/updateActivity`),
					method: 'post',
					data: this.$http.adornData({
						'id': this.formcomp.id,
						'state': this.formcomp.state,
						'title': this.formcomp.title,
						'url': this.formcomp.url,
						'imageUrl': this.formcomp.imageUrl,
						'status': this.statusIdd
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible2 = false
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

			//添加精选商品
			choiaddNotice() {
				this.campus = ''
				this.dialogFormVisible5 = true
				this.getMyLocation()
			},

			// 搜索
			shousuo() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl4(
						`/supersearch/apikey/maxd/back/100/sort/2/keyword/${this.keyword}/is_coupon/1/tb_p/1/min_id/1`
					),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					console.log(data)
					if (data && data.code === 1) {
						this.tableDataLoading = false
						let returnData = data.data
						this.choicenData2 = returnData
					}
				})
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.status = -1
				this.classify = 0
				this.myPhone = ''
				this.search = ''
				this.page = 1
				this.campus = ''
				console.log('this.phone', this.myPhone)
				this.InformationSelect()
			},
			// 获取社区数据列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`activity/selectActivity`),
					method: 'get',
					params: this.$http.adornParams({

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.homeData1 = [{
						id: 0,
						title: '全部'
					}]
					if (data.data) {
						for (var i in data.data) {
							this.homeData1.push(data.data[i])
							data.data[i].state = Number(data.data[i].state)
						}
					}
					console.log(data.data.state)
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
						'campus': this.campus
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
			// 封面图片上传
			handleAvatarSuccess5(file) {
				this.titleImg = file.data;
			},
			// 详情图片上传
			handleRemove(file) {
				console.log(file, this.img)
				this.img.push(file.data);
			},
			// 删除详情图
			dels(index) {
				this.img.splice(index, 1);
				console.log(this.img)
			},
			// 获取分类id
			onChang(e) {
				console.log(e)
				this.classifyId = e
			},
			// 获取社区
			onChang3(e) {
				console.log(e)
				this.campus = e
			},
			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.district = value[2]
				this.form.city = value[1]
				if (this.city == '市辖区') {
					this.city = this.province
				}
				if (this.form.city == '市辖区') {
					this.city = this.province
				}
				console.log(this.$refs['cascaderAddr'].currentLabels)
			},
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("__TENCENT_MAP_KEY__", "小地图");
				geolocation.getIpLocation(this.showPosition, this.showErr);
				// geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
			},
			showPosition(position) {
				console.log(position);
				// this.latitude = position.lat;
				// this.longitude = position.lng;
				// this.city = position.city;
				this.setMap();
			},
			showErr(e) {
				console.log("定位失败", e);
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
				//设置地图中心点
				var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
				//定义工厂模式函数
				var myOptions = {
					zoom: 13, //设置地图缩放级别
					center: myLatlng, //设置中心点样式
					mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
				}
				// //获取dom元素添加地图信息
				var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
				//给地图添加点击事件

				//给定位的位置添加图片标注
				var marker = new qq.maps.Marker({
					position: myLatlng,
					map: map
				});
				// `````````````
				var that = this;
				if (that.longitude == '') {
					var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
				} else {
					var center = new qq.maps.LatLng(that.latitude, that.longitude);
				}

				var map = new qq.maps.Map(document.getElementById("container1"), {
					center: center,
					zoom: 13
				});
				var marker = new qq.maps.Marker({
					position: center,
					map: map
				});
				var latlngBounds = new qq.maps.LatLngBounds();
				qq.maps.event.addListener(map, "click", function(event) {
					console.log(event, qq.maps);
					that.longitude = event.latLng.getLng(); // 经度
					that.latitude = event.latLng.getLat(); // 纬度
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng.getLng() + '&key=__TENCENT_MAP_KEY__&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response)
						that.address = response.result.formatted_addresses.recommend

					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
					if (markersArray) {
						for (let i in markersArray) {
							markersArray[i].setMap(null);
						}
					}
					var marker = new qq.maps.Marker({
						map: map,
						position: event.latLng
					});
					markersArray.push(marker);

				});
				geocoder = new qq.maps.Geocoder({
					complete: function(result) {
						console.log(result);
						that.longitude = result.detail.location.lng;
						that.latitude = result.detail.location.lat;
						map.setCenter(result.detail.location);
						var marker = new qq.maps.Marker({
							map: map,
							position: result.detail.location
						});
						markersArray.push(marker);
					}
				});
			},
			// 地图定位
			select() {
				console.log(this.address)
				var add = this.province + this.city + this.district + this.address
				let that = this
				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + add +
					'&key=__TENCENT_MAP_KEY__&get_poi=1&output=jsonp', {
						myCustomUrlParam: 'veryNice'
					}).then(response => {
					// handle success
					if (response.message == '查询无结果') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '查询无结果',
							type: 'warning'
						});
						return
					}
					console.log('response', response)
					that.longitude = response.result.location.lng; // 经度
					that.latitude = response.result.location.lat; // 纬度
					// that.address = response.result.address_components.province + response.result.address_components
					// 	.city + response.result.address_components.district + response.result.title
					that.setMap()
				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
			},
			// 获取用户列表弹框
			userselect() {
				this.dialogFormVisible3 = true
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				let phone = -1
				if (this.phone) {
					phone = this.phone
				}
				let nickName = -1
				if (this.nickName) {
					nickName = this.nickName
				}
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'member': this.member,
						'phone': this.phone
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.page = 1
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phone = ''
				this.nickName = ''
				this.tableDataLoading4 = true

				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userIdss = row.id
				this.nickName = row.nickName
				this.phone = row.phone
				if (this.nickName == '' || this.nickName == null) {
					this.nickName = row.phone
				}
				this.dialogFormVisible3 = false
			},
			// 添加标签
			btnTj() {
				if (this.bq == '' || this.bq == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签名',
						type: 'warning'
					});
					return
				} else {
					console.log('this.bq', this.bq)
					this.labels.push(this.bq)
					this.bq = ''
				}

			},
			// 删除标签
			dels1(index) {
				this.labels.splice(index, 1);
				console.log(this.labels)
			},
			// 确定添加
			addmissionNoticeTo() {
				if (this.address != '') {
					this.select()
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					});
					return
				}
				if (this.titleImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传任务封面图',
						type: 'warning'
					});
					return
				}
				var reg = /^1[3|4|5|7|8][0-9]{9}$/; //验证规则
				if (!reg.test(this.phone)) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入有效的手机号',
						type: 'warning'
					});
					return
				}
				// if (this.address == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入地址详情',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// toString()
				let urls = ''
				if (this.missions == 0) {
					urls = 'information/insertInformation'
				} else {
					urls = 'information/updateInformation'
				}
				let that = this
				setTimeout(function() {
					that.$http({
						url: that.$http.adornUrl(urls),
						method: 'post',
						data: that.$http.adornData({
							'name': that.name,
							'label': that.labels.toString(),
							'address': that.address,
							'phone': that.phone,
							'titleImg': that.titleImg,
							'img': that.img.toString(),
							'classifyId': that.classifyId,
							'latitude': that.latitude,
							'longitude': that.longitude,
							'remark': that.remark,
							'area': that.area,
							'house': that.house,
							'price': that.price,
							'province': that.province,
							'userId': that.userIdss,
							'id': that.ids,
							'campus': that.campus
						})
					}).then(({
						data
					}) => {
						console.log('提交结果', data)
						that.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								that.prev()
								that.campus = ''
								// that.name = ''
								// that.labels = []
								// that.address = ''
								// that.phone = ''
								// that.titleImg = ''
								// that.img = []
								// that.classifyId = ''
								// that.latitude = ''
								// that.longitude = ''
								// that.remark = ''
								// that.area = ''
								// that.house = ''
								// that.price = ''
								// that.province = ''
								// that.userIdss = ''
								// that.ids = ''
								that.InformationSelect()
							}
						})

					})
				}, 1000)
			},
			// 关闭添加信息弹框
			prev() {
				this.dialogFormVisible5 = false
				this.campus = ''
				this.name = ''
				this.labels = []
				this.address = ''
				this.phone = ''
				this.titleImg = ''
				this.img = []
				this.classifyId = ''
				this.latitude = ''
				this.longitude = ''
				this.remark = ''
				this.area = ''
				this.house = ''
				this.price = ''
				this.province = ''
				this.userIdss = ''
				this.ids = ''
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
				this.labels = []
				this.missions = 1
				if (this.missions == 1) {
					if (row.label) {
						this.labels = row.label.split(',');
					}
					this.address = row.address;
					this.name = row.name;
					this.phone = row.phone;
					this.titleImg = row.titleImg;
					if (row.img) {
						this.img = row.img.split(',');
					}
					this.classifyId = row.classifyId;
					this.latitude = row.latitude;
					this.longitude = row.longitude;
					this.remark = row.remark;
					this.area = row.area;
					this.house = row.house;
					this.price = row.price;
					this.province = row.province;
					this.userId = row.userId;
					this.url = row.url;
					this.ids = row.id;
					this.campus = row.campus;
				}
				this.dialogFormVisible5 = true
				this.getMyLocation()
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
			animeDat2(state) {
				this.InformationSelect()
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
					url: this.$http.adornUrl('helpCampus/selectCampusPage'),
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
			// 社区轮播图是否启用
			change(val, row) {
				this.$http({
					url: this.$http.adornUrl(`banner/updateBannerStateById?id=${row.id}`),
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
							this.homeSelect1()
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
			}
		},
		mounted() {
			this.homeSelect1()
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
