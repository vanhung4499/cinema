<template>
  <div>
    <!-- Breadcrumb Navigation Area -->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">Home</el-breadcrumb-item>
        <el-breadcrumb-item>Movie Management</el-breadcrumb-item>
        <el-breadcrumb-item>Movie Information</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-card class="box-card">
      <!-- Search and Filter Section -->
      <el-row :gutter="20" class="row1">
        <el-col :span="5">
          <el-input v-model="inputMovieName" placeholder="Enter movie name" clearable></el-input>
        </el-col>
        <el-col :span="5">
          <el-select class="el-select-search" v-model="selectedMovieArea" placeholder="Select movie area" clearable>
            <el-option
              v-for="item in movieAreaList"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button class="el-button-search" icon="el-icon-search" @click="getMovieList">Search</el-button>
        </el-col>
      </el-row>

      <!-- Date Range Selection and Actions -->
      <el-row :gutter="20" class="row2">
        <el-col :span="10">
          <el-date-picker
            :unlink-panels="true"
            v-model="selectedDate"
            type="datetimerange"
            range-separator="to"
            start-placeholder="Start Date"
            end-placeholder="End Date">
          </el-date-picker>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addDialogVisible = true">Add Movie</el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="danger" @click="isAbleMultipleDelete">Batch Delete</el-button>
        </el-col>
      </el-row>

      <!-- Movie Information Table -->
      <el-table :data="movieList" style="width: 100%" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="movieId" label="#" width="40"></el-table-column>
        <el-table-column prop="movieName" label="Movie Name"></el-table-column>
        <el-table-column prop="movieArea" label="Movie Area"></el-table-column>
        <el-table-column prop="movieLength" label="Duration (minutes)"></el-table-column>
        <el-table-column prop="releaseDate" label="Release Date"></el-table-column>
        <el-table-column prop="movieBoxOffice" label="Box Office"></el-table-column>
        <!-- Action Buttons -->
        <el-table-column label="Action" width="240">
          <template slot-scope="scope">
            <el-tooltip effect="dark" content="View Movie Details" placement="top" :enterable="false" :open-delay="500">
              <el-button type="success" icon="el-icon-view" size="mini" @click="toMovieInfo(scope.row.movieId)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Edit Movie Information" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="isAbleEdit(scope.row.movieId)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Delete Movie" placement="top" :enterable="false" :open-delay="500">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="isAbleDelete(scope.row.movieId)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Manage Movie Categories" placement="top" :enterable="false" :open-delay="500">
              <el-button type="warning" icon="el-icon-setting" size="mini" @click="showEditCategoryDialog(scope.row.movieId)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pageNum"
        :page-sizes="[5, 7, 9]"
        :page-size="queryInfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-card>

    <!-- Add Movie Dialog -->
    <el-dialog title="Add Movie" :visible.sync="addDialogVisible" width="60%" @close="addDialogClosed">
      <!-- Content Section -->
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px">
        <!-- Movie Name -->
        <el-form-item label="Movie Name" prop="movieName">
          <el-input v-model="addForm.movieName"></el-input>
        </el-form-item>
        <!-- Movie Area -->
        <el-form-item label="Movie Area" prop="movieArea">
          <el-select v-model="addForm.movieArea" placeholder="Select movie area" clearable>
            <el-option
              v-for="item in movieAreaList"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <!-- Movie Length -->
        <el-form-item label="Duration (minutes)" prop="movieLength">
          <el-input v-model="addForm.movieLength"></el-input>
        </el-form-item>
        <!-- Release Date -->
        <el-form-item label="Release Date" prop="releaseDate">
          <el-date-picker
            v-model="addForm.releaseDate"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="Select date">
          </el-date-picker>
        </el-form-item>
        <!-- Movie Introduction -->
        <el-form-item label="Movie Introduction" prop="movieIntroduction">
          <el-input v-model="addForm.movieIntroduction"></el-input>
        </el-form-item>
        <!-- Movie Poster -->
        <el-form-item label="Movie Poster">
          <el-upload
            action=""
            list-type="picture-card"
            :auto-upload="false"
            :limit="1"
            :file-list="poster"
            :on-exceed="handleExceed"
            :on-change="handleChangeP"
            :on-success="handleSuccessP"
            :on-error="handleError"
            ref="posterRef"
            :http-request="submitFileP">
            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{file}">
              <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
              <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-preview" @click="handlePictureCardPreviewP(file)">
              <i class="el-icon-zoom-in"></i>
            </span>
            <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemoveP(file)">
              <i class="el-icon-delete"></i>
            </span>
          </span>
            </div>
          </el-upload>
          <!-- Image Preview Dialog -->
          <el-dialog :visible.sync="dialogVisible" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>
        <!-- Movie Gallery -->
        <el-form-item label="Movie Gallery">
          <el-upload
            action=""
            list-type="picture-card"
            :auto-upload="false"
            :file-list="pics"
            :on-change="handleChange"
            :on-success="handleSuccess"
            :on-error="handleError"
            ref="pictureRef"
            :http-request="submitFile">
            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{file}">
              <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
              <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
              <i class="el-icon-zoom-in"></i>
            </span>
            <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
              <i class="el-icon-delete"></i>
            </span>
          </span>
            </div>
          </el-upload>
          <!-- Image Preview Dialog -->
          <el-dialog :visible.sync="dialogVisible" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>
      </el-form>
      <!-- Footer Buttons -->
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="addMovie">Confirm</el-button>
  </span>
    </el-dialog>

    <!-- Edit Movie Information Dialog -->
    <el-dialog title="Edit Movie Information" :visible.sync="editDialogVisible" width="60%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px">
        <!-- Movie Name -->
        <el-form-item label="Movie Name" prop="movieName">
          <el-input v-model="editForm.movieName" disabled></el-input>
        </el-form-item>

        <!-- Movie Area -->
        <el-form-item label="Movie Area" prop="movieArea">
          <el-select v-model="editForm.movieArea" placeholder="Select movie area" clearable>
            <el-option
              v-for="item in movieAreaList"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>

        <!-- Movie Length -->
        <el-form-item label="Duration (minutes)" prop="movieLength">
          <el-input v-model="editForm.movieLength"></el-input>
        </el-form-item>

        <!-- Release Date -->
        <el-form-item label="Release Date" prop="releaseDate">
          <el-date-picker
            v-model="editForm.releaseDate"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="Select date">
          </el-date-picker>
        </el-form-item>

        <!-- Movie Introduction -->
        <el-form-item label="Movie Introduction" prop="movieIntroduction">
          <el-input v-model="editForm.movieIntroduction"></el-input>
        </el-form-item>

        <!-- Edit Movie Poster -->
        <el-form-item label="Movie Poster">
          <el-upload
            action=""
            list-type="picture-card"
            :auto-upload="false"
            :limit="1"
            :file-list="poster"
            :on-exceed="handleExceed"
            :on-change="handleChangeP"
            :on-success="handleSuccessP"
            :on-error="handleError"
            ref="posterEditRef"
            :http-request="submitFileP">
            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{file}">
              <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
              <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-preview" @click="handlePictureCardPreviewP(file)">
              <i class="el-icon-zoom-in"></i>
            </span>
            <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemoveP(file)">
              <i class="el-icon-delete"></i>
            </span>
          </span>
            </div>
          </el-upload>
          <!-- Image Preview Dialog -->
          <el-dialog :visible.sync="dialogVisible" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>

        <!-- Edit Movie Gallery -->
        <el-form-item label="Movie Gallery">
          <el-upload
            action=""
            list-type="picture-card"
            :auto-upload="false"
            :file-list="pics"
            :on-change="handleChange"
            :on-success="handleSuccess"
            :on-error="handleError"
            ref="pictureEditRef"
            :http-request="submitFile">
            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{file}">
              <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
              <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
              <i class="el-icon-zoom-in"></i>
            </span>
            <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
              <i class="el-icon-delete"></i>
            </span>
          </span>
            </div>
          </el-upload>
          <!-- Image Preview Dialog -->
          <el-dialog :visible.sync="dialogVisible" append-to-body>
            <img width="100%" :src="dialogImageUrl" alt="">
          </el-dialog>
        </el-form-item>
      </el-form>
      <!-- Footer Buttons -->
      <span slot="footer" class="dialog-footer">
    <el-button @click="editDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="editMovieInfo">Confirm</el-button>
  </span>
    </el-dialog>

    <!-- Movie Category Management Dialog -->
    <el-dialog title="Movie Category Management" :visible.sync="editCategoryVisible" width="60%" @close="editCategoryDialogClosed">
      <!-- Form for managing movie categories -->
      <el-form label-width="150px">
        <!-- Select dropdown for choosing movie categories -->
        <el-form-item label="Movie Categories" prop="movieActor">
          <el-select v-model="selectedMovieCategory" placeholder="Please select category name" clearable>
            <!-- Iterate through categoryList to display options -->
            <el-option
              v-for="item in categoryList"
              :key="item.movieCategoryId"
              :label="item.movieCategoryName"
              :value="item.movieCategoryId">
            </el-option>
          </el-select>
          <!-- Button to add selected category -->
          <el-button type="primary" @click="addCategory">Add</el-button>
        </el-form-item>
        <!-- Display selected categories as tags -->
        <el-form-item>
          <el-tag
            v-for="tag in editCategoryForm"
            :key="tag.movieCategoryId"
            closable
            @close="deleteCategory(tag.movieCategoryId)">
            {{ tag.movieCategoryName }}
          </el-tag>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "MovieInfo",
  data() {
    return {
      queryInfo: {
        movieId: '',
        movieArea: '',
        movieName: '',
        startDate: '',
        endDate: '',
        pageNum: 1,
        pageSize: 7
      },
      total: 0,
      url: 'http://localhost:8181/',
      // Control dialog visibility
      addDialogVisible: false,
      selectedMovieCategory: [],
      selectedMovieArea: '',
      selectedDate: [],
      inputMovieName: '',
      categoryList: [],
      movieList: [],
      movieAreaList: [
        "Vietnam", "China", "United States", "South Korea", "Japan", "Hong Kong",
        "Taiwan", "Thailand", "India", "France", "United Kingdom", "Russia",
        "Italy", "Spain", "Germany", "Poland", "Australia", "Iran", "Other"
      ],
      editForm: {},
      checkAbleId: {},
      editCategoryForm: [],
      movieId: '',
      editDialogVisible: false,
      editCategoryVisible: false,
      multipleSelection: [],
      dialogVisible: false,
      dialogImageUrl: '',
      disabled: false,
      // Movie poster variables for add and delete
      poster: [],
      // JSON image to send to backend
      posterL: [],
      deletePostList: [],
      // Add and delete image collection variables, dynamically bind image list
      pics: [],
      // JSON array of images to send to backend
      pictureList: [],
      picNums: 0,
      deletePicList: [],
      // Add movie form data
      addForm: {
        movieId: '',
        movieName: '',
        movieArea: '',
        movieLength: 0,
        releaseDate: '',
        movieBoxOffice: '',
        movieIntroduction: '',
        moviePictures: '',
        moviePoster: ''
      },
      // Add validation rules for form
      addFormRules: {
        movieName: [
          { required: true, message: 'Please enter the movie name', trigger: 'blur' }
        ],
        movieArea: [
          { required: true, message: 'Please select the movie area', trigger: 'blur' }
        ]
      },
      // Edit validation rules for form
      editFormRules: {
        movieName: [
          { required: true, message: 'Please enter the movie name', trigger: 'blur' }
        ],
        movieArea: [
          { required: true, message: 'Please select the movie area', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getMovieList()
    this.getCategoryList()
  },
  methods: {
    getMovieList() {
      this.queryInfo.movieName = this.inputMovieName
      this.queryInfo.movieArea = this.selectedMovieArea
      if (!this.selectedDate) {
        this.queryInfo.startDate = this.selectedDate[0]
        this.queryInfo.endDate = this.selectedDate[1]
      }
      const _this = this;
      axios.get('sysMovie/find', {params: _this.queryInfo}).then(resp => {
        console.log(resp)
        _this.movieList = resp.data.data;
        _this.total = resp.data.total;
        _this.queryInfo.pageSize = resp.data.pageSize;
        _this.queryInfo.pageNum = resp.data.pageNum;
      })
    },
    getCategoryList() {
      const _this = this
      axios.get('sysMovieCategory/find').then(resp=>{
        _this.categoryList = resp.data.data;
        console.log(_this.categoryList)
      })
    },
    ifShow(id) {
      let l = true
      for (let i = 0; i < this.editCategoryForm.length; i++) {
        if (id === this.editCategoryForm[i].movieCategoryId) {
          l =false
        }
      }
      return l
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize
      this.getMovieList()
      console.log(newSize)
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage
      this.getMovieList()
      console.log(newPage)
    },
    async submitFile() {
      const _this = this
      for (let i = 0; i < this.pics.length; i++) {
        let formData = new FormData()
        if (this.pics[i].status === 'success') {
          let s = this.pics[i].url
          this.pictureList.push(s.substring(s.indexOf('/images')))
          continue
        }
        let file = this.pics[i].raw
        formData.append('file', file)
        await axios.post('upload/movie', formData).then(response => {
          _this.pictureList.push(response.data.data)
        })
      }
    },
    async submitFileP() {
      const _this = this
      for (let i = 0; i < this.poster.length; i++) {
        let formData = new FormData()
        if (this.poster[i].status === 'success') {
          let s = this.poster[i].url
          this.posterL.push(s.substring(s.indexOf('/images')))
          continue
        }
        let file = this.poster[i].raw
        formData.append('file', file)
        await axios.post('upload/movie', formData).then(response => {
          _this.posterL.push(response.data.data)
        })
      }
    },
// Close event listener for the add dialog
    addDialogClosed() {
      this.$refs.addFormRef.resetFields();    // Reset form fields
      this.$refs.pictureRef.clearFiles();     // Clear uploaded pictures
      this.$refs.posterRef.clearFiles();      // Clear uploaded poster
      this.poster = [];                       // Clear poster array
      this.posterL = [];                      // Clear posterL array
      this.pics = [];                         // Clear pics array
      this.pictureList = [];                  // Clear pictureList array
    },

// Click listener for the add button
    async addMovie() {
      await this.submitFile();                // Upload picture files
      await this.submitFileP();               // Upload poster files
      this.addForm.moviePictures = JSON.stringify(this.pictureList);  // Convert to JSON string
      this.addForm.moviePoster = JSON.stringify(this.posterL);         // Convert to JSON string

      const _this = this;
      this.$refs.addFormRef.validate(async valid => {
        console.log(valid);
        if (!valid) return;

        // Pre-validation successful, send network request
        axios.defaults.headers.post['Content-Type'] = 'application/json';
        await axios.post('sysMovie', JSON.stringify(_this.addForm)).then(resp => {
          console.log(resp);
          if (resp.data.code !== 200) {
            this.$message.error('Failed to add movie information!');
          }
        });

        // Hide add dialog
        this.addDialogVisible = false;

        // Reload the list
        this.getMovieList();
        this.$message.success('Successfully added movie information!');
      });
    },

// Close event listener for the edit dialog
    editDialogClosed() {
      this.$refs.editFormRef.resetFields();    // Reset form fields
      this.$refs.pictureEditRef.clearFiles();  // Clear uploaded pictures
      this.$refs.posterEditRef.clearFiles();   // Clear uploaded poster
      this.pics = [];                         // Clear pics array
      this.pictureList = [];                  // Clear pictureList array
      this.posterL = [];                      // Clear posterL array
      this.poster = [];                       // Clear poster array
    },

// Close event listener for the edit category dialog
    editCategoryDialogClosed() {
      this.selectedMovieCategory = '';        // Clear selected movie category
    },

// Edit movie information dialog
    async editMovieInfo() {
      await this.submitFile();  // Upload picture files
      this.editForm.moviePictures = JSON.stringify(this.pictureList);  // Convert to JSON string
      await this.submitFileP();  // Upload poster files
      this.editForm.moviePoster = JSON.stringify(this.posterL);  // Convert to JSON string

      this.$refs.editFormRef.validate(async valid => {
        const _this = this;
        if (!valid) return;
        let success = true;
        axios.defaults.headers.put['Content-Type'] = 'application/json';
        await axios.put('sysMovie', JSON.stringify(_this.editForm)).then(resp => {
          if (resp.data.code !== 200) {
            this.$message.error('Failed to modify movie information!');
            success = false;
          }
        });
        if (!success) return;

        // Delete uploaded pictures
        for (let s of this.deletePicList) {
          await axios.get('/upload/delete?filePath=' + s.substring(s.indexOf('/images')));
        }

        // Delete uploaded poster
        for (let s of this.deletePostList) {
          await axios.get('/upload/delete?filePath=' + s.substring(s.indexOf('/images')));
        }

        // Close edit dialog
        this.editDialogVisible = false;

        // Reload the movie list
        this.getMovieList();
        this.$message.success('Successfully modified movie information!');
      });
    },

// Handle change in picture files
    handleChange(file, fileList) {
      this.pics = fileList.slice(0);
      console.log(this.pics);
    },

// Handle change in poster files
    handleChangeP(file, fileList) {
      this.poster = fileList;
      console.log(this.poster);
    },

// Handle successful picture upload
    handleSuccess(response) {
      this.pictureList.push(response.data);
      this.addForm = JSON.stringify(this.pictureList);
      this.editForm = JSON.stringify(this.pictureList);
    },

// Handle successful poster upload
    handleSuccessP(response) {
      this.posterL.push(response.data);
      this.addForm = JSON.stringify(this.posterL);
      this.editForm = JSON.stringify(this.posterL);
    },

// Preview picture in card view
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },

// Preview poster in card view
    handlePictureCardPreviewP(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },

// Remove picture file
    handleRemove(file, fileList) {
      const filePath = file.url;
      console.log(filePath);
      const idx = this.pics.findIndex(x => x.url === filePath);
      if (file.status === 'success') {
        this.deletePicList.push(file.url);
      }
      this.pics.splice(idx, 1);
    },

// Remove poster file
    handleRemoveP(file) {
      const filePath = file.url;
      console.log(filePath);
      const idx = this.poster.findIndex(x => x.url === filePath);
      if (file.status === 'success') {
        this.deletePostList.push(file.url);
      }
      this.poster.splice(idx, 1);
    },

// Handle upload error
    handleError(err) {
      console.log(err);
    },

// Redirect to movie information page
    toMovieInfo(id) {
      window.open('http://localhost:8081/movieInfo/' + id);
    },

// Check if movie can be edited
    isAbleEdit(id) {
      this.checkAbleId.movieId = id;
      axios.get('sysSession/isAbleEdit', { params: this.checkAbleId }).then(response => {
        console.log(response.data.total);
        let sessions = response.data.data;
        if (response.data.total === 0) {
          console.log('Empty and can edit');
          this.showEditDialog(id);
        } else {
          console.log('There are unfinished sessions before modification');
          let sessionIds = '';
          for (let temp of sessions) {
            console.log(temp);
            sessionIds += temp.sessionId + ' ';
          }
          console.log('sessionIds is : ' + sessionIds);
          this.$alert('Sorry! There are unfinished movie sessions, cannot modify movie information.\n' +
            'The session IDs causing the exception are: ' + sessionIds, 'Modification Request Exception Notice', {
            confirmButtonText: 'Got it',
            callback: action => {
              this.$router.push('/movie');
            }
          });
        }
      });
    },

// Show edit dialog and populate data
    async showEditDialog(id) {
      const _this = this;
      await axios.get('sysMovie/find/' + id).then(resp => {
        console.log(resp);
        _this.editForm = resp.data.data;
      });
      for (const item of JSON.parse(this.editForm.moviePictures)) {
        let pic = {};
        pic['name'] = '';
        pic['url'] = this.global.base + item;
        this.pics.push(pic);
      }
      for (const item of JSON.parse(this.editForm.moviePoster)) {
        let pic = {};
        pic['name'] = '';
        pic['url'] = this.global.base + item;
        this.poster.push(pic);
      }
      this.editDialogVisible = true;
    },

// Show edit category dialog and populate data
    async showEditCategoryDialog(id) {
      const _this = this;
      _this.movieId = id;
      await axios.get('sysMovie/find/' + id).then(response => {
        _this.editCategoryForm = response.data.data.movieCategoryList;
      });
      this.editCategoryVisible = true;
    },

// Cancel editing
    cancelEdit() {
      this.editDialogVisible = false;
      this.deletePicList.splice(0, this.deletePicList.length);
      this.deletePostList.splice(0, this.deletePostList.length);
    },

// Handle selection change in multiple selection
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

// Validate before multiple deletion
    async isAbleMultipleDelete() {
      let ableDelete = true;
      let ids = '';
      for (let i = 0; i < this.multipleSelection.length; i++) {
        this.checkAbleId.movieId = this.multipleSelection[i].movieId;
        const { data: res } = await axios.get('sysSession/isAbleEdit', { params: this.checkAbleId });
        if (res.data.length !== 0) {
          ids += this.multipleSelection[i].movieId + ' ';
          ableDelete = false;
        }
      }
      if (ableDelete === false) {
        this.$alert('Sorry! There are unfinished movie sessions, cannot delete movie information in batch.\n' +
          'The movie IDs causing the exception are: ' + ids, 'Batch Deletion Request Exception Notice', {
          confirmButtonText: 'Got it',
          callback: action => {
            this.$router.push('/movie');
          }
        });
        return;
      }
      await this.multipleDelete();
    },

// Perform batch deletion of movies
    async multipleDelete() {
      const _this = this;
      // Ask user for confirmation to delete
      const resp = await this.$confirm('This will permanently delete these items. Continue?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err);

      // If user confirms deletion, resp is "confirm"
      // If user cancels deletion, resp is "cancel"
      if (resp === 'cancel') {
        return _this.$message.info('Deletion canceled');
      }

      let ids = [];
      this.multipleSelection.forEach(data => {
        let item = data.movieId;
        ids.push(item);
      });
      console.log(ids);
      axios.defaults.headers.post['Content-Type'] = 'application/json';
      await axios.delete('sysMovie/' + ids, JSON.stringify(ids)).then(resp => {
        console.log(123);
        if (resp.data.code !== 200) {
          this.$message.success('Failed to delete movies in batch!');
        }
      });
      this.getMovieList();
      this.$message.success('Successfully deleted movies in batch!');
    },

// Check if it's able to delete a single movie
    async isAbleDelete(id) {
      this.checkAbleId.movieId = id;
      await axios.get('sysSession/isAbleEdit', { params: this.checkAbleId }).then(response => {
        console.log(response.data.total);
        let sessions = response.data.data;
        if (response.data.total === 0) {
          this.deleteMovieById(id);
        } else {
          console.log('Before deletion, there are unfinished sessions');
          let sessionIds = '';
          for (let temp of sessions) {
            console.log(temp);
            sessionIds += temp.sessionId + ' ';
          }
          console.log('Session IDs: ' + sessionIds);
          this.$alert('Sorry! There are unfinished movie sessions, cannot delete movie information.\n' +
            'The session IDs causing the exception are: ' + sessionIds, 'Deletion Request Exception Notice', {
            confirmButtonText: 'Got it',
            callback: action => {
              this.$router.push('/movie');
            }
          });
        }
      });
    },

// Delete a single movie
    async deleteMovieById(id) {
      let deleteInfo = id;
      const _this = this;
      // Ask user for confirmation to delete
      const resp = await this.$confirm('This will permanently delete this item. Continue?', 'Warning', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err);

      // If user confirms deletion, resp is "confirm"
      // If user cancels deletion, resp is "cancel"
      console.log(resp);
      if (resp === 'cancel') {
        return _this.$message.info('Deletion canceled');
      }

      console.log(deleteInfo);
      axios.defaults.headers.post['Content-Type'] = 'application/json';
      await axios.delete('sysMovie/' + deleteInfo, JSON.stringify(deleteInfo)).then(resp => {
        if (resp.data.code !== 200) {
          _this.$message.success('Failed to delete movie information!');
        }
      });
      this.getMovieList();
      this.$message.success('Successfully deleted movie information!');
    },

// Delete a category
    async deleteCategory(categoryId) {
      console.log('Category ID:');
      console.log(categoryId);
      const _this = this;
      await axios.delete('sysMovieToCategory/' + this.movieId + '/' + categoryId).then(resp => {
        console.log(resp);
        _this.$message.success('Deleted category successfully');
      });
      await axios.get('sysMovie/find/' + this.movieId).then(response => {
        _this.editCategoryForm = response.data.data.movieCategoryList;
      });
    },

// Handle exceed file limit
    handleExceed() {
      this.$message.error('Movie poster cannot exceed one!');
    },

// Add a category
    async addCategory() {
      const _this = this;
      await axios.post('sysMovieToCategory/' + this.movieId + '/' + this.selectedMovieCategory).then(resp => {
        console.log(resp);
        if (resp.data.code !== 200) return this.$alert('Failed to add movie category', 'Add Movie Category Exception Notice', {
          confirmButtonText: 'Got it'
        });
        this.$message.success("Added movie category successfully");
      });
      axios.get('sysMovie/find/' + this.movieId).then(response => {
        _this.editCategoryForm = response.data.data.movieCategoryList;
      });
    }

  }
}
</script>

<style scoped>
.row2{
  margin-top: 20px;
}
.el-tag{
    margin: 0 10px 10px 0;
}
.el-button-search{
  margin: 0px 0px 0px 10px;
}
.el-select-search{
  margin: 0 0px 0px 20px;
}
</style>
