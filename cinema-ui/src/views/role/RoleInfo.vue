<template>
  <div>
    <!-- Breadcrumb Navigation Area -->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">Home</el-breadcrumb-item>
        <el-breadcrumb-item>Permission Management</el-breadcrumb-item>
        <el-breadcrumb-item>Role Information Management</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- Card View -->
    <el-card>
      <el-row :gutter="20">
        <el-col :span="2.5">
          <el-button type="primary" @click="addDialogVisible = true">Add Role</el-button>
        </el-col>
        <el-col :span="2">
          <el-button type="danger" @click="isAbleMultipleDelete">Batch Delete</el-button>
        </el-col>
      </el-row>

      <el-table :data="roleList" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column prop="roleId" label="Role ID" width="80"></el-table-column>
        <el-table-column prop="roleName" label="Role Name" width="180"></el-table-column>
        <el-table-column prop="roleDesc" label="Role Description"></el-table-column>
        <el-table-column label="Operation" width="200">
          <template slot-scope="scope">
            <el-tooltip effect="dark" content="Edit Role Information" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" icon="el-icon-edit" size="mini" @click="showEditDialog(scope.row.roleId)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Delete Role" placement="top" :enterable="false" :open-delay="500">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="isAbleDelete(scope.row.roleId)"></el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="Assign Permissions" placement="top" :enterable="false" :open-delay="500">
              <el-button type="warning" icon="el-icon-setting" size="mini" @click="showSetRightDialog(scope.row)"></el-button>
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

    <!-- Add Role Dialog -->
    <el-dialog title="Add Role" :visible.sync="addDialogVisible" width="50%" @close="addDialogClosed">
      <!-- Content Area -->
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
        <el-form-item label="Role Name" prop="roleName">
          <el-input v-model="addForm.roleName"></el-input>
        </el-form-item>
        <el-form-item label="Role Description" prop="roleDesc">
          <el-input v-model="addForm.roleDesc"></el-input>
        </el-form-item>
      </el-form>
      <!-- Footer Area -->
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="addRole">Confirm</el-button>
  </span>
    </el-dialog>

    <!-- Edit Role Dialog -->
    <el-dialog title="Edit Role" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
        <el-form-item label="Role ID" prop="roleId">
          <el-input v-model="editForm.roleId" disabled></el-input>
        </el-form-item>
        <el-form-item label="Role Name" prop="roleName">
          <el-input v-model="editForm.roleName"></el-input>
        </el-form-item>
        <el-form-item label="Role Description" prop="roleDesc">
          <el-input v-model="editForm.roleDesc"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="editDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="editRole">Confirm</el-button>
  </span>
    </el-dialog>

    <!-- Assign Permissions Dialog -->
    <el-dialog
      title="Assign Permissions"
      :visible.sync="setRightDialogVisible"
      width="50%"
      @close="setRightDialogClosed">
      <!-- Tree Component -->
      <el-tree :data="resourceList"
               :props="treeProps"
               node-key="id"
               :default-checked-keys="defKeys"
               ref="treeRef"
               show-checkbox
               default-expand-all></el-tree>
      <span slot="footer" class="dialog-footer">
    <el-button @click="setRightDialogVisible = false">Cancel</el-button>
    <el-button type="primary" @click="submitRights">Confirm</el-button>
  </span>
    </el-dialog>


  </div>
</template>

<script>
import moment from "moment";
export default {
  name: "RoleInfo",
  data() {
    return {
      queryInfo: {
        query: '',
        pageNum: 1,
        pageSize: 7
      },
      roleList: [],
      resourceList: [],
      // Properties for the tree component
      treeProps: {
        children: 'children',
        label: 'name'
      },
      // Default checked keys for permissions
      defKeys: [],
      total: 0,
      addDialogVisible: false,
      // Form data for adding a role
      addForm: {
        roleName: '',
        roleDesc: ''
      },
      // Validation rules for the add role form
      addFormRules: {
        roleName: [
          { required: true, message: 'Please enter the role name', trigger: 'blur' }
        ],
        roleDesc: [
          { required: true, message: 'Please enter the role description', trigger: 'blur' }
        ]
      },
      editDialogVisible: false,
      editForm: {
        roleId: null,
        roleName: '',
        roleDesc: ''
      },
      checkAbleId: {},
      // Validation rules for the edit role form
      editFormRules: {
        roleName: [
          { required: true, message: 'Please enter the role name', trigger: 'blur' }
        ],
        roleDesc: [
          { required: true, message: 'Please enter the role description', trigger: 'blur' }
        ]
      },
      multipleSelection: [],
      // Visibility of the assign permissions dialog
      setRightDialogVisible: false,
      // Current role ID for assigning permissions
      roleId: ''
    };
  },
  created() {
    this.getRoleList()
  },
  methods: {
    async getRoleList() {
      const {data: res} = await axios.get('sysRole', {params: this.queryInfo})
      this.roleList = res.data;
      this.total = res.total
      this.queryInfo.pageNum = res.pageNum
      this.queryInfo.pageSize = res.pageSize
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize
      this.getRoleList()
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage
      this.getRoleList()
    },
// Listen for the close event of the add dialog
    addDialogClosed() {
      this.$refs.addFormRef.resetFields();
    },

// Listen for the add role button click
    addRole() {
      const _this = this;
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return;
        // Validation passed, send network request

        axios.defaults.headers.post['Content-Type'] = 'application/json';
        const { data: res } = await axios.post('sysRole', JSON.stringify(_this.addForm));
        if (res.code !== 200) return this.$message.error(res.msg);
        // Hide the add dialog
        this.addDialogVisible = false;
        // Reload the list
        await this.getRoleList();
        this.$message.success('Role added successfully!');
      });
    },

// Show edit dialog and fetch data for editing
    async showEditDialog(id) {
      const _this = this;
      await axios.get('sysRole/' + id).then(resp => {
        console.log(resp);
        _this.editForm = resp.data.data;
      });
      this.editDialogVisible = true;
    },

// Listen for the close event of the edit dialog
    editDialogClosed() {
      this.$refs.editFormRef.resetFields();
    },

// Edit role information and submit
    editRole() {
      this.$refs.editFormRef.validate(async valid => {
        const _this = this;
        if (!valid) return;
        axios.defaults.headers.put['Content-Type'] = 'application/json';
        const { data: res } = await axios.put('sysRole/', JSON.stringify(_this.editForm));
        if (res.code !== 200) return this.$message.error(res.msg);

        this.editDialogVisible = false;
        await this.getRoleList();
        this.$message.success('Role information updated successfully!');
      });
    },

// Listen for selection change in the table
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

// Pre-check before performing batch delete
    async isAbleMultipleDelete() {
      let ableMultipleDelete = true;
      let ids = '';
      for (let i = 0; i < this.multipleSelection.length; i++) {
        this.checkAbleId.roleId = this.multipleSelection[i].roleId;
        const { data: res } = await axios.get('sysUser', { params: this.checkAbleId });
        if (res.data.length !== 0) {
          ids += this.multipleSelection[i].roleId + ' ';
          ableMultipleDelete = false;
        }
      }
      if (!ableMultipleDelete) {
        this.$alert('Sorry! There are users associated with the roles you are trying to delete. The role information cannot be deleted.\n' + 'Roles causing the exception: ' + ids, 'Batch Delete Request Exception Notice', {
          confirmButtonText: 'Got it',
          callback: action => {
            this.$router.push('/role');
          }
        });
        return;
      }
      await this.multipleDelete();
    },

// Perform batch delete operation
    async multipleDelete() {
      const _this = this;
      // Ask the user to confirm the deletion
      const resp = await this.$confirm('This action will permanently delete these items, continue?', 'Warning', {
        confirmButtonText: 'Yes',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err);

      // User confirms deletion, resp is "confirm"
      // User cancels deletion, resp is "cancel"
      if (resp === 'cancel') {
        return _this.$message.info('Deletion canceled');
      }

      let ids = [];
      this.multipleSelection.forEach(data => {
        ids.push(data.roleId);
      });
      let success = true;
      await axios.delete('sysRole/' + ids).then(resp => {
        if (resp.data.code !== 200) {
          success = false;
        }
      });
      if (!success) return this.$message.error('Failed to delete roles in batch!');
      await this.getRoleList();
      this.$message.success('Roles deleted in batch successfully!');
    },

// Check if role deletion is possible
    async isAbleDelete(id) {
      this.checkAbleId.roleId = id;
      await axios.get('sysUser', { params: this.checkAbleId }).then(response => {
        console.log(response.data.total);
        let users = response.data.data;
        if (response.data.total === 0) {
          console.log('Empty and can be deleted');
          this.deleteRoleById(id);
        } else {
          console.log('Check before deletion, related users found');
          let ids = '';
          for (let temp of users) {
            console.log(temp);
            ids += temp.userId + ' ';
          }
          console.log('Related users: ' + ids);
          this.$alert('Sorry! There are users associated with the role you are trying to delete. The role information cannot be deleted.\n' + 'Users causing the exception: ' + ids, 'Delete Request Exception Notice', {
            confirmButtonText: 'Got it',
            callback: action => {
              this.$router.push('/role');
            }
          });
        }
      });
    },

// Delete resource by id
    async deleteRoleById(roleId) {
      const _this = this;
      // Ask the user to confirm deletion
      const resp = await this.$confirm('This action will permanently delete this item, continue?', 'Prompt', {
        confirmButtonText: 'Yes',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).catch(err => err);

      // User confirms deletion, resp is "confirm"
      // User cancels deletion, resp is "cancel"
      console.log(resp);
      if (resp === 'cancel') {
        return _this.$message.info('Deletion canceled');
      }

      let success = true;
      await axios.delete('sysRole/' + roleId).then(resp => {
        if (resp.data.code !== 200) {
          success = false;
        }
      });
      if (!success) return _this.$message.error('Failed to delete role information!');
      await this.getRoleList();
      this.$message.success('Role information deleted successfully!');
    },

// Display assign permissions dialog
    async showSetRightDialog(role) {
      this.roleId = role.roleId;
      const { data: res } = await axios.get('sysResource/tree');
      this.resourceList = res.data;
      console.log(role);
      await this.getLeafKeys(role, this.defKeys);
      console.log('defKeys: ' + this.defKeys);
      this.setRightDialogVisible = true;
    },

// Get all specific permission IDs corresponding to the role
    getLeafKeys(node, arr) {
      if (!node) return;
      if (!node.children) {
        return arr.push(node.id);
      }
      node.children.forEach(item => this.getLeafKeys(item, arr));
    },

// Close assign permissions dialog and refresh defKeys
    setRightDialogClosed() {
      this.defKeys = [];
    },

// Submit assigned rights
    async submitRights() {
      const keys = [
        ...this.$refs.treeRef.getCheckedKeys(true)
      ];
      const _this = this;
      axios.defaults.headers.post['Content-Type'] = 'application/json';
      const { data: res } = await axios.post('sysRole/' + _this.roleId, JSON.stringify(keys));
      if (res.code !== 200) return this.$message.error("Failed to update permissions!");

      this.$message.success("Permissions updated successfully!");
      await this.getRoleList();
      this.setRightDialogVisible = false;
    }

  }
}
</script>

<style scoped>

</style>
