<template>
  <div>
    <!-- Breadcrumb navigation -->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">Home</el-breadcrumb-item>
        <el-breadcrumb-item>Movies Management</el-breadcrumb-item>
        <el-breadcrumb-item>Movie Categories</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- Card view -->
    <el-card class="box-card">
      <el-row :gutter="20">
        <el-col :span="1.5">
          <el-button type="primary" @click="addDialogVisible = true">Add</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="danger" @click="multipleDelete">Batch Delete</el-button>
        </el-col>
      </el-row>

      <!-- Category list -->
      <el-table :data="movieCategoryList" style="width: 45%" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="movieCategoryId" label="Category ID" width="145"></el-table-column>
        <el-table-column prop="movieCategoryName" label="Movie Category" width="180"></el-table-column>
        <el-table-column label="Actions" width="150">
          <template slot-scope="scope">
            <el-tooltip effect="dark" content="Edit Movie Category" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.movieCategoryId)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Delete Category" placement="top" :enterable="false" :open-delay="500">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteMovieCategoryById(scope.row.movieCategoryId)"></el-button>
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

    <!-- Add Category Dialog -->
    <el-dialog title="Add Category" :visible.sync="addDialogVisible" width="50%" @close="addDialogClosed">
      <!-- Content area -->
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px">
        <!-- prop: defines validation rules in addFormRules, v-model: two-way binding data -->
        <el-form-item label="Category" prop="movieCategoryName">
          <el-input v-model="addForm.movieCategoryName"></el-input>
        </el-form-item>
      </el-form>
      <!-- Footer area -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="addMovieCategory">Confirm</el-button>
      </span>
    </el-dialog>

    <!-- Edit Category Dialog -->
    <el-dialog title="Edit Category" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px">
        <el-form-item label="Category ID">
          <el-input v-model="editForm.movieCategoryId" disabled></el-input>
        </el-form-item>
        <el-form-item label="Category" prop="movieCategoryName">
          <el-input v-model="editForm.movieCategoryName"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="editMovieCategoryInfo">Confirm</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "MovieCategory",
  // Vue.prototype.$message = Message
  // Vue.prototype.$confirm = MessageBox.confirm
  data() {
    return {
      queryInfo: {
        query: '',
        pageNum: 1,
        pageSize: 7
      },
      movieCategoryList: [],
      total: 0,
      // Control dialog visibility
      addDialogVisible: false,
      // Form data for adding category
      addForm: {
        movieCategoryName: ''
      },
      // Form validation rules
      addFormRules: {
        movieCategoryName: [
          { required: true, message: 'Please enter movie category', trigger: 'blur' }
        ]
      },
      editDialogVisible: false,
      editForm: {},
      // Form validation rules for editing
      editFormRules: {
        movieCategoryName: [
          { required: true, message: 'Please enter movie category', trigger: 'blur' }
        ]
      },
      // Selected items in table for batch operations
      multipleSelection: []
    }
  },
  created() {
    this.getMovieCategoryList()
  },
  methods: {
    getMovieCategoryList() {
      const _this = this;
      axios.get('sysMovieCategory/find', {params: _this.queryInfo}).then(resp => {
        console.log(resp)
        _this.movieCategoryList = resp.data.data;
        _this.total = resp.data.total;
        _this.queryInfo.pageSize = resp.data.pageSize;
        _this.queryInfo.pageNum = resp.data.pageNum;
      })
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize
      this.getMovieCategoryList()
      console.log(newSize)
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage
      this.getMovieCategoryList()
      console.log(newPage)
    },
    // Handle closing of the add dialog
    addDialogClosed() {
      // Reset form fields in the add dialog
      this.$refs.addFormRef.resetFields();
    },

    // Listen to add button click
    addMovieCategory() {
      const _this = this;
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return;

        // Pre-validation successful, send network request
        axios.defaults.headers.post['Content-Type'] = 'application/json';
        await axios.post('sysMovieCategory', JSON.stringify(_this.addForm)).then(resp => {
          if (resp.data.code !== 200) {
            this.$message.error('Failed to add movie category!');
            return;
          }
        });

        // Hide add dialog
        this.addDialogVisible = false;

        // Reload the category list
        this.getMovieCategoryList();
        this.$message.success('Successfully added movie category!');
      });
    },

// Show edit dialog and populate data
    showEditDialog(id) {
      const _this = this;
      axios.get('sysMovieCategory/' + id).then(resp => {
        _this.editForm = resp.data.data;
      });
      this.editDialogVisible = true;
    },

// Listen to close event of edit dialog
    editDialogClosed() {
      this.$refs.editFormRef.resetFields();
    },

// Edit movie category information and submit
    editMovieCategoryInfo() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return;

        axios.defaults.headers.put['Content-Type'] = 'application/json';
        await axios.put('sysMovieCategory', JSON.stringify(this.editForm)).then(resp => {
          if (resp.data.code !== 200) {
            this.$message.error('Failed to modify movie category!');
            return;
          }
        });

        this.editDialogVisible = false;
        this.getMovieCategoryList();
        this.$message.success('Successfully modified movie category!');
      });
    },

// Listen to selection change in the table
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

// Perform multiple deletion of movie categories
    async multipleDelete() {
      const _this = this;

      // Confirm with the user before deleting
      const resp = await this.$confirm('This operation will permanently delete these items. Continue?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err);

      // User confirms deletion, resp is "confirm"
      // User cancels deletion, resp is "cancel"
      if (resp === 'cancel') {
        return _this.$message.info('Deletion cancelled');
      }

      let ids = [];
      this.multipleSelection.forEach(data => {
        ids.push(data.movieCategoryId);
      });

      await axios.delete('sysMovieCategory/' + ids).then(resp => {
        if (resp.data.code !== 200) {
          this.$message.success('Failed to delete movie categories in batch!');
        }
      });

      this.getMovieCategoryList();
      this.$message.success('Successfully deleted movie categories in batch!');
    },

    // Delete movie category by id
    async deleteMovieCategoryById(id) {
      const _this = this;

      // Confirm with the user before deleting
      const resp = await this.$confirm('This operation will permanently delete this item. Continue?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err);

      // User confirms deletion, resp is "confirm"
      // User cancels deletion, resp is "cancel"
      if (resp === 'cancel') {
        return _this.$message.info('Deletion cancelled');
      }

      // Send delete request to delete movie category
      await axios.delete('sysMovieCategory/' + id).then(resp => {
        if (resp.data.code !== 200) {
          _this.$message.error('Failed to delete movie category!');

        }
      });

      // Refresh movie category list after deletion
      this.getMovieCategoryList();
      this.$message.success('Successfully deleted movie category!');
    }

  }
}
</script>

<style scoped>

</style>
