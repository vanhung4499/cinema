<template>
  <div class="register-container">
    <div class="header" @click="toWelcome">
      <img src="../assets/homeLogo.png" style="width: 115px; height: 60px;">
    </div>
    <div class="register-body">
      <div class="register-icon">
        <img src="../assets/icon-login.svg">
      </div>
      <el-form class="register-form" ref="registerFormRef" :model="registerForm" :rules="registerFormRules" label-width="150px">
        <el-form-item label="Username" prop="userName">
          <el-input v-model="registerForm.userName" placeholder="Please enter your username" clearable></el-input>
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="Please enter your password" show-password></el-input>
        </el-form-item>
        <el-form-item label="Confirm Password" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="Please confirm your password" show-password></el-input>
        </el-form-item>
        <el-form-item label="Phone Number" prop="phoneNumber">
          <el-input v-model="registerForm.phoneNumber" type="text" aria-placeholder="Please enter your phone number" clearable></el-input>
        </el-form-item>
        <el-form-item label="Gender">
          <el-radio v-model="gender" label="1">Male</el-radio>
          <el-radio v-model="gender" label="0">Female</el-radio>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :round="true" @click="register">Agree and Register</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please enter your password again'))
        // password is the field bound on the form
      } else if (value !== this.registerForm.password) {
        callback(new Error('The two passwords entered do not match!'))
      } else {
        callback()
      }
    }
    let checkMobile = (rule, value, cb) => {
      const regMobile = /(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\b/
      if (regMobile.test(value)){
        return cb()
      }
      cb(new Error('Please enter a valid phone number'))
    }
    return {
      gender: '1',
      registerForm: {
        userName: '',
        password: '',
        confirmPassword: '',
        phoneNumber: '',
        sex: ''
      },
      registerFormRules: {
        userName: [
          { required: true, message: "Please enter a username", trigger: "blur"},
          { min: 2, max: 20, message: "Username length between 2 and 20 characters", trigger: "blur"}
        ],
        password: [
          { required: true, message: "Please enter a password", trigger: "blur"},
          { min:6, max: 16, message: "Length between 6 and 16 characters", trigger: "blur"}
        ],
        confirmPassword: [
          { required: true, validator: validatePass, message: "The two passwords do not match", trigger: "blur"},
        ],
        phoneNumber: [
          { required: true, message: 'Please enter a phone number', trigger: 'blur' },
          { validator: checkMobile, trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    register() {
      this.$refs.registerFormRef.validate(async valid => {
        if (!valid) return
        this.registerForm.sex = this.gender === '1'
        axios.defaults.headers.post['Content-Type'] = 'application/json'
        const { data: res } = await axios.post('sysUser/register', JSON.stringify(this.registerForm));
        if (res.code !== 200) return this.$message.error(res.msg);

        this.$message.success("Registration completed!");
        await this.$router.push('/login')
        this.$refs.registerFormRef.resetFields()
      })
    },
    toWelcome() {
      this.$router.push('/welcome')
    }
  }
}
</script>

<style scoped>
.register-container{
  width: 100%;
  height: 100%;
}

.header{
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
  border-bottom: deepskyblue solid 2px;
  padding: 6px 0;
  cursor: pointer;
}

.register-body {
  //display: flex;
  margin: 30px 0;
}

.register-icon {
  display: flex;
  justify-content: center;
  align-items: center;
}

.register-form{
  width: 400px;
  margin: 30px auto;
}

.footer-mini{
  border-top: 1px solid #EEE;
  padding-top: 20px;
  text-align: center;
}

.footer{
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
