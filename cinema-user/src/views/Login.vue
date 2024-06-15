<template>
  <div class="login-container">
    <div class="header" @click="toWelcome">
      <img src="../assets/homeLogo.png" style="width: 115px; height: 60px;">
    </div>
    <div class="login-body">
      <div class="login-icon">
        <img src="../assets/icon-login.svg">
      </div>
      <!-- Login Form Area -->
      <el-form class="login-form" :model="loginForm" :rules="loginFormRules" ref="loginFormRef" label-width="150px">
        <!-- Username -->
        <el-form-item label="Username" prop="userName">
          <el-input v-model="loginForm.userName" placeholder="Enter your username" clearable prefix-icon="iconfont icon-user"></el-input>
        </el-form-item>
        <!-- Password -->
        <el-form-item label="Password" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="Enter your password" show-password prefix-icon="iconfont icon-lock"></el-input>
        </el-form-item>
        <!-- Button -->
        <el-form-item>
          <el-button size="medium" :round="true" type="primary" @click="login">Login</el-button>
          <el-button size="medium" :round="true" type="warning" @click="registerAccount">Register</el-button>
        </el-form-item>
      </el-form>
    </div>

  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      // Login form data object
      loginForm: {
        userName: 'admin',
        password: '123456'
      },
      // Login form validation rules
      loginFormRules: {
        // Validate username format
        userName: [
          { required: true, message: "Please enter the username", trigger: "blur"},
          { min:2, max: 20, message: "Username must be between 2 and 20 characters", trigger: "blur"}
        ],
        // Validate password format
        password: [
          { required: true, message: "Please enter the password", trigger: "blur"},
          { min:6, max: 16, message: "Password must be between 6 and 16 characters", trigger: "blur"}
        ]
      },
      sessionId: 0
    }
  },
  created() {
    this.sessionId = window.sessionStorage.getItem('sessionId')
    console.log('this sessionId is : ' + this.sessionId)
    window.sessionStorage.setItem('sessionId', 0)
  },
  methods: {
    success(params) {
      this.login()
    },
    login() {
      this.$refs.loginFormRef.validate(async valid => {
        if (!valid) return;
        axios.defaults.headers.post['Content-Type'] = 'application/json';
        try {
          const { data: res } = await axios.post('sysUser/login', JSON.stringify(this.loginForm));
          if (res.code !== 200) return this.$message.error(res.msg);

          this.$message.success({ message: "Login successful", duration: 1000 });
          console.log(res.data);
          // Save token
          window.sessionStorage.setItem("token", res.data.token);
          res.data.sysUser.sysRole = null;
          window.sessionStorage.setItem("loginUser", JSON.stringify(res.data.sysUser));
          if (this.sessionId !== 0 && this.sessionId !== '0' && this.sessionId !== null) {
            await this.$router.push('/chooseSeat/' + this.sessionId);
            return;
          }
          // Navigate to the homepage
          await this.$router.push('/welcome');
        } catch (error) {
          console.error("Error occurred during login:", error);
          this.$message.error("An error occurred during login. Please try again later.");
        }
      });
    },
    registerAccount() {
      this.$router.push('/register')
    },
    toWelcome() {
      this.$router.push('/welcome')
    }
  }
}
</script>

<style scoped>
.login-container {
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

.login-body {
  margin: 30px 0;
}

.login-icon {
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-form {
  width: 400px;
  margin: 30px auto;
}

</style>
