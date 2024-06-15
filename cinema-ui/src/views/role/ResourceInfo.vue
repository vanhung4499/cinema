<template>
  <div>
    <!-- Breadcrumb Navigation Area -->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">Home</el-breadcrumb-item>
        <el-breadcrumb-item>Permission Management</el-breadcrumb-item>
        <el-breadcrumb-item>Resource Information Management</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- Card View -->
    <el-card>
      <el-row :gutter="20">
        <el-col :span="2">
          <el-button type="primary" @click="addDialogVisible = true">Add</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="danger" @click="multipleDelete">Batch Delete</el-button>
        </el-col>
      </el-row>

      <el-table :data="resourceList" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column prop="id" label="Resource ID" width="120"></el-table-column>
        <el-table-column prop="name" label="Resource Name"></el-table-column>
        <el-table-column prop="path" label="Resource Path" ></el-table-column>
        <el-table-column prop="level" label="Permission Level" width="150">
          <template slot-scope="scope">
            <el-tag type="danger" v-if="scope.row.level === 1">Level 1</el-tag>
            <el-tag type="warning" v-else-if="scope.row.level === 2">Level 2</el-tag>
            <el-tag v-else>Level 3</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="parent.name" label="Parent Resource Name"></el-table-column>
        <el-table-column label="Actions" width="150">
          <template slot-scope="scope">
            <el-tooltip effect="dark" content="Edit Resource Information" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.id)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Delete Resource" placement="top" :enterable="false" :open-delay="500">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteResourceById(scope.row.id)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination Area -->
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


    <!-- Add Resource Dialog -->
    <el-dialog title="Add Resource" :visible.sync="addDialogVisible" width="60%" @close="addDialogClosed">
      <!-- Content Area -->
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
        <el-form-item label="Resource ID" prop="id">
          <el-input v-model="addForm.id"></el-input>
        </el-form-item>
        <el-form-item label="Resource Name" prop="name">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="Resource Path" prop="path">
          <el-input v-model="addForm.path"></el-input>
        </el-form-item>
        <el-form-item label="Parent Resource Name" prop="parentId">
          <el-select v-model="addForm.parentId" placeholder="Select Parent Resource" clearable>
            <el-option
              v-for="item in allResources"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <!-- Footer Area -->
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="addResource">Confirm</el-button>
  </span>
    </el-dialog>

    <!-- Edit Resource Dialog -->
    <el-dialog title="Edit Resource" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
        <el-form-item label="Resource ID" prop="id">
          <el-input v-model="editForm.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="Resource Name" prop="name">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="Resource Path" prop="path">
          <el-input v-model="editForm.path"></el-input>
        </el-form-item>
        <el-form-item label="Parent Resource Name" prop="parentId">
          <el-select v-model="editForm.parentId" placeholder="Select Parent Resource" clearable>
            <el-option
              v-for="item in allResources"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <!-- Footer Area -->
      <span slot="footer" class="dialog-footer">
    <el-button @click="editDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="editResource">Confirm</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "ResourceInfo",
  data() {
    return {
      queryInfo: {
        query: '',
        pageNum: 1,
        pageSize: 7
      },
      resourceList: [],
      allResources: [],
      total: 0,
      addDialogVisible: false,
      addForm: {
        name: '',
        path: '',
        level: 0,
        parentId: null
      },
      addFormRules: {
        name: [
          { required: true, message: 'Please enter resource name', trigger: 'blur' }
        ],
        path: [
          { required: true, message: 'Please enter resource path', trigger: 'blur'}
        ]
      },
      editDialogVisible: false,
      editForm: {},
      editFormRules: {
        name: [
          { required: true, message: 'Please enter resource name', trigger: 'blur' }
        ],
        path: [
          { required: true, message: 'Please enter resource path', trigger: 'blur'}
        ]
      },
      multipleSelection: []
    }
  },
  created() {
    this.getResourceList()
    this.getAllResource()
  },
  methods: {
    async getResourceList(){
      const {data : res} = await axios.get('sysResource', {params: this.queryInfo})
      this.resourceList = res.data;
      this.total = res.total
      this.queryInfo.pageNum = res.pageNum
      this.queryInfo.pageSize = res.pageSize
    },
    async getAllResource(){
      const {data : res} = await axios.get('sysResource')
      this.allResources = res.data;
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize
      this.getResourceList()
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage
      this.getResourceList()
    },
// Listen for the close event of the add dialog
    addDialogClosed() {
      this.$refs.addFormRef.resetFields();
    },

// Listen for the add button
    async addResource() {
      try {
        await this.$refs.addFormRef.validate(async (valid) => {
          if (!valid) return;

          // Pre-validation successful, send network request
          if (this.addForm.parentId === null) this.addForm.parentId = 0;

          const response = await axios.post('sysResource', JSON.stringify(this.addForm));
          const res = response.data;

          if (res.code !== 200) {
            this.$message.error(res.msg);
            return;
          }

          // Hide the add dialog
          this.addDialogVisible = false;
          // Reload the list
          await this.getResourceList();
          this.$message.success('Resource added successfully!');
        });
      } catch (error) {
        console.error('Error adding resource:', error);
        this.$message.error('Failed to add resource, please try again!');
      }
    },

// Show edit dialog and populate data
    async showEditDialog(id) {
      try {
        const response = await axios.get(`sysResource/${id}`);
        this.editForm = response.data.data;
        if (this.editForm.parentId === 0) this.editForm.parentId = '';
        this.editDialogVisible = true;
      } catch (error) {
        console.error('Error fetching resource data:', error);
      }
    },

// Listen for the close event of the edit dialog
    editDialogClosed() {
      this.$refs.editFormRef.resetFields();
    },

// Edit resource category information and submit
    editResource() {
      this.$refs.editFormRef.validate(async (valid) => {
        if (!valid) return;

        if (this.editForm.parentId === '') this.editForm.parentId = 0;

        axios.defaults.headers.put['Content-Type'] = 'application/json';
        try {
          const response = await axios.put('sysResource/', JSON.stringify(this.editForm));
          const res = response.data;

          if (res.code !== 200) {
            this.$message.error(res.msg);
            return;
          }

          this.editDialogVisible = false;
          await this.getResourceList();
          this.$message.success('Resource information updated successfully!');
        } catch (error) {
          console.error('Error updating resource information:', error);
          this.$message.error('Failed to update resource information, please try again!');
        }
      });
    },

// Listen for selection change in the table
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

// Handle multiple deletion of resources
    async multipleDelete() {
      // Ask user for confirmation
      const resp = await this.$confirm('This operation will permanently delete these items. Continue?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err);

      // User confirmed deletion, resp is "confirm"
      // User canceled deletion, resp is "cancel"
      if (resp === 'cancel') {
        return this.$message.info('Deletion canceled');
      }

      // Extract IDs from selected items
      let ids = this.multipleSelection.map(data => data.id);

      // Perform deletion
      try {
        const response = await axios.delete('sysResource/' + ids);
        const res = response.data;

        if (res.code !== 200) {
          this.$message.error('Failed to delete resource information!');
          return;
        }

        await this.getResourceList();
        this.$message.success('Batch deletion of resource information successful!');
      } catch (error) {
        console.error('Error deleting resources:', error);
        this.$message.error('Failed to delete resource information, please try again!');
      }
    },

// Delete resource by ID
    async deleteResourceById(id) {
      // Ask user for confirmation
      const resp = await this.$confirm('This operation will permanently delete this item. Continue?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err);

      // User confirmed deletion, resp is "confirm"
      // User canceled deletion, resp is "cancel"
      if (resp === 'cancel') {
        return this.$message.info('Deletion canceled');
      }

      // Perform deletion
      try {
        const response = await axios.delete('sysResource/' + id);
        const res = response.data;

        if (res.code !== 200) {
          this.$message.error('Failed to delete resource information!');
          return;
        }

        await this.getResourceList();
        this.$message.success('Resource information deleted successfully!');
      } catch (error) {
        console.error('Error deleting resource:', error);
        this.$message.error('Failed to delete resource information, please try again!');
      }
    }

  }
}
</script>

<style scoped>

</style>
