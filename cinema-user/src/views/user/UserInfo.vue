<template>
  <div class="user-info-container">
    <div style="width: 30%">
      <h1>User Avatar</h1>
      <el-upload
        action=""
        list-type="picture-card"
        :auto-upload="false"
        :limit="1"
        :file-list="pics"
        :on-change="handleChange"
        :on-success="handleSuccess"
        :on-error="handleError"
        :on-exceed="handleExceed"
        ref="pictureRef"
        :http-request="submitFile"
      >
        <i slot="default" class="el-icon-plus"></i>
        <div slot="file" slot-scope="{file}">
          <img class="el-upload-list__item-thumbnail" :src="(picDialog ? '' : global.base) + file.url" alt="">
          <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-delete" @click="handleRemove(file)">
              <i class="el-icon-delete"></i>
            </span>
          </span>
        </div>
      </el-upload>
    </div>
    <div style="width: 70%">
      <el-form :model="loginUser" :rules="loginUserRules" ref="editFormRef" label-width="80px">
        <el-form-item label="Username" prop="userName">
          <el-input v-model="loginUser.userName"></el-input>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="loginUser.email"></el-input>
        </el-form-item>
        <el-form-item label="Phone Number" prop="phoneNumber">
          <el-input v-model="loginUser.phoneNumber"></el-input>
        </el-form-item>
        <el-form-item label="Gender" prop="sex">
          <el-radio v-model="loginUser.sex" :label="true">Male</el-radio>
          <el-radio v-model="loginUser.sex" :label="false">Female</el-radio>
        </el-form-item>
        <el-form-item label="Birthday" prop="birthday">
          <el-date-picker
            v-model="loginUser.birthday"
            value-format="yyyy-MM-dd"
            type="date"
            placeholder="Select Date"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="Signature" prop="autograph">
          <el-input v-model="loginUser.autograph"></el-input>
        </el-form-item>
        <el-button type="primary" @click="editUser">Save</el-button>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserInfo",
  data() {
    let checkEmail = (rule, value, cb) => {
      const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/
      if (regEmail.test(value)){
        return cb()
      }
      cb(new Error('Please enter a valid email'))
    }
    let checkMobile = (rule, value, cb) => {
      const regMobile = /^(0|86|17951)?(13[0-9]|15[0123456789]|17[678]|18[0-9]|14[57])[0-9]{8}$/
      if (regMobile.test(value)){
        return cb()
      }
      cb(new Error('Please enter a valid mobile number'))
    }
    return {
      picDialog:false,
      loginUser:{},
      pics:[],
      pictureList:[],
      picSrc:"",
      deletePicList:[],

      // Form validation rules object
      loginUserRules: {
        userName: [
          { required: true, message: 'Please enter username', trigger: 'blur' }
        ],
        email: [
          { required: true, message: 'Please enter email', trigger: 'blur' },
          { validator: checkEmail, trigger: 'blur'}
        ],
        phoneNumber: [
          { required: true, message: 'Please enter phone number', trigger: 'blur' },
          { validator: checkMobile, trigger: 'blur'}
        ],
        sex: [
          { required: true, message: 'Please select gender', trigger: 'change' }
        ],
        birthday: [
          { required: true, message: 'Please select birthday', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getUser()
  },
  methods:{
    async getUser(){
      const { data : res } = await axios.get('sysUser/'+JSON.parse(window.sessionStorage.getItem('loginUser')).userId)
      this.loginUser = res.data
      this.pics.push({name:'picture',url:JSON.parse(this.loginUser.userPicture)[0]})
    },
    async handleSuccess(response, file, fileList){
      this.pics = fileList
      console.log(this.pics)
      await this.submitFile()
      this.picSrc = this.pictureList[0]
    },
    handleError(err){
      console.log(err)
    },
    handleChange(file, filelist){
      this.pics = filelist.slice(0)
      console.log(111)
      console.log(this.pics)
      this.picDialog =true
    },
    handleRemove(file) {
      const filePath = file.url
      console.log(filePath)
      const idx = this.pics.findIndex(x => x.url === filePath)
      if(file.status === 'success'){
        this.deletePicList.push(file.url)
      }
      this.pics.splice(idx, 1)
    },
    async editUser(){
      await this.submitFile()
      this.loginUser.userPicture = JSON.stringify(this.pictureList)
      const _this = this
      let success = true
      axios.defaults.headers.put['Content-Type'] = 'application/json'
      await axios.put('sysUser',JSON.stringify(this.loginUser)).then(resp=>{
        if (resp.data.code !== 200){
          this.$message.error('Failed to modify user information!')
          success = false
        }
      })
      if(!success) return
      this.$message.success('User information modified successfully!')
      this.pics = []
      this.pictureList = []
      await this.getUser()
      this.picDialog = false
    },
    async submitFile(){
      const _this = this
      for (let i = 0; i < this.pics.length; i++){
        let formData = new FormData()
        if (this.pics[i].status === 'success') {
          let s = this.pics[i].url
          this.pictureList.push(s.substring(s.indexOf('/images')))
          continue
        }
        let file = this.pics[i].raw
        formData.append('file', file)
        await axios.post('upload/user', formData).then(response =>{
          console.log(response)
          _this.pictureList.push(response.data.data)
        })
      }
    },
    handleExceed(){
      this.$message.error("You can upload at most one user avatar!")
    },
  }
}
</script>

<style scoped>
.user-info-container{
  display: flex;
}
</style>
