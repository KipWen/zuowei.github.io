<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

const students = ref([]);
const isLoading = ref(true);
const error = ref(null);
const showDialog = ref(false);
const currentStudent = ref({
  id: null,
  name: "",
  studentId: "",
  grade: "",
  className: "",
  gender: 0,
});
const isEditing = ref(false);

// 获取学生列表
const fetchStudents = async () => {
  isLoading.value = true;
  error.value = null;
  try {
    const response = await axios.get("/api/students");
    if (response.data.code === "200") {
      students.value = response.data.data;
    } else {
      error.value = response.data.msg;
    }
  } catch (err) {
    console.error("获取学生信息失败", err);
    error.value = err.message || "获取学生信息失败";
  } finally {
    isLoading.value = false;
  }
};

// 初始化表单
const initForm = () => {
  currentStudent.value = {
    id: null,
    name: "",
    studentId: "",
    grade: "",
    className: "",
    gender: 0,
  };
  isEditing.value = false;
};

// 打开添加对话框
const openAddDialog = () => {
  initForm();
  showDialog.value = true;
};

// 打开编辑对话框
const openEditDialog = (student) => {
  currentStudent.value = { ...student };
  isEditing.value = true;
  showDialog.value = true;
};

// 保存学生信息
const saveStudent = async () => {
  try {
    if (isEditing.value) {
      // 更新学生
      await axios.put(
        `/api/students/${currentStudent.value.id}`,
        currentStudent.value
      );
    } else {
      // 添加学生
      await axios.post("/api/students", currentStudent.value);
    }
    showDialog.value = false;
    fetchStudents();
  } catch (err) {
    console.error("保存学生信息失败", err);
    error.value = err.response?.data?.msg || "保存学生信息失败";
  }
};

// 删除学生
const deleteStudent = async (id) => {
  if (!confirm("确定要删除这个学生吗？")) return;

  try {
    await axios.delete(`/api/students/${id}`);
    fetchStudents();
  } catch (err) {
    console.error("删除学生失败", err);
    error.value = err.response?.data?.msg || "删除学生失败";
  }
};

// 查看学生详情
const viewStudent = (student) => {
  currentStudent.value = { ...student };
  isEditing.value = false;
  showDialog.value = true;
};

onMounted(() => {
  fetchStudents();
});
</script>

<template>
  <div class="student-container">
    <div class="header">
      <h2>学生信息管理</h2>
      <p class="subtitle">当前共 {{ students.length }} 名学生</p>
      <button @click="openAddDialog" class="add-btn">添加学生</button>
    </div>

    <div class="table-wrapper">
      <div v-if="isLoading" class="loading">
        <div class="spinner"></div>
        <p>正在加载学生数据...</p>
      </div>

      <div v-else-if="error" class="error-message">
        {{ error }}
        <button @click="fetchStudents" class="retry-btn">重试</button>
      </div>

      <div v-else class="table-responsive">
        <table class="student-table">
          <thead>
            <tr>
              <th>序号</th>
              <th>学生姓名</th>
              <th>学号</th>
              <th>年级</th>
              <th>班级</th>
              <th>性别</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(student, index) in students" :key="student.id">
              <td>{{ index + 1 }}</td>
              <td>{{ student.name }}</td>
              <td>{{ student.studentId }}</td>
              <td>{{ student.grade }}</td>
              <td>{{ student.className || "-" }}</td>
              <td>{{ student.gender === 0 ? "男" : "女" }}</td>
              <td>
                <button @click="viewStudent(student)" class="action-btn view">
                  查看
                </button>
                <button
                  @click="openEditDialog(student)"
                  class="action-btn edit"
                >
                  编辑
                </button>
                <button
                  @click="deleteStudent(student.id)"
                  class="action-btn delete"
                >
                  删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 学生信息对话框 -->
    <div v-if="showDialog" class="dialog-overlay">
      <div class="dialog-content">
        <h3>
          {{
            isEditing ? "编辑学生" : currentStudent.id ? "学生详情" : "添加学生"
          }}
        </h3>

        <div class="form-group">
          <label>姓名</label>
          <input
            v-model="currentStudent.name"
            :disabled="!isEditing && currentStudent.id"
          />
        </div>

        <div class="form-group">
          <label>学号</label>
          <input
            v-model="currentStudent.studentId"
            :disabled="!isEditing && currentStudent.id"
          />
        </div>

        <div class="form-group">
          <label>年级</label>
          <input
            v-model="currentStudent.grade"
            :disabled="!isEditing && currentStudent.id"
          />
        </div>

        <div class="form-group">
          <label>班级</label>
          <input
            v-model="currentStudent.className"
            :disabled="!isEditing && currentStudent.id"
          />
        </div>

        <div class="form-group">
          <label>性别</label>
          <select
            v-model="currentStudent.gender"
            :disabled="!isEditing && currentStudent.id"
          >
            <option value="0">男</option>
            <option value="1">女</option>
          </select>
        </div>

        <div class="dialog-actions">
          <button
            v-if="isEditing || !currentStudent.id"
            @click="saveStudent"
            class="save-btn"
          >
            {{ isEditing ? "保存修改" : "添加学生" }}
          </button>
          <button @click="showDialog = false" class="cancel-btn">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 原有样式保持不变，新增以下样式 */

.student-container {
  max-width: 1100px;
  margin: 2rem auto;
  padding: 0 1rem;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
}

.header {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  border-bottom: 2px solid #e1e4e8;
  padding-bottom: 1rem;
  margin-bottom: 1.5rem;
}

.header h2 {
  font-weight: 700;
  font-size: 1.8rem;
  color: #222;
}

.subtitle {
  flex: 1;
  text-align: center;
  font-size: 1rem;
  color: #666;
}

.add-btn {
  padding: 0.6rem 1.3rem;
  background: linear-gradient(135deg, #4caf50, #388e3c);
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.3s ease;
  white-space: nowrap;
}

.add-btn:hover {
  background: linear-gradient(135deg, #45a049, #2e7d32);
  box-shadow: 0 2px 8px rgba(67, 160, 71, 0.5);
}

.table-wrapper {
  overflow-x: auto;
  border-radius: 8px;
  box-shadow: 0 0 10px #ddd;
}

.student-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 8px;
  font-size: 1rem;
  min-width: 700px;
}

.student-table th,
.student-table td {
  padding: 0.7rem 1rem;
  text-align: left;
  vertical-align: middle;
}

.student-table thead th {
  background-color: #f5f7fa;
  color: #555;
  font-weight: 600;
  border-bottom: 2px solid #ddd;
}

.student-table tbody tr {
  background: #fff;
  box-shadow: 0 1px 3px rgb(0 0 0 / 0.07);
  transition: background-color 0.25s ease;
  border-radius: 6px;
  cursor: default;
}

.student-table tbody tr:hover {
  background-color: #f0f8ff;
}

.spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #4caf50;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  animation: spin 1s linear infinite;
  margin-bottom: 8px;
  margin-left: auto;
  margin-right: auto;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.error-message {
  color: #d93025;
  font-weight: 600;
  text-align: center;
  padding: 1rem 0;
}

.retry-btn {
  margin-top: 0.5rem;
  background-color: #d93025;
  border: none;
  padding: 0.4rem 1rem;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.retry-btn:hover {
  background-color: #a62518;
}

.action-btn {
  border: none;
  padding: 0.4rem 0.8rem;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 600;
  font-size: 0.9rem;
  margin-right: 6px;
  transition: background-color 0.3s, box-shadow 0.3s;
  user-select: none;
}

.action-btn.view {
  background-color: #2196f3;
  color: white;
}

.action-btn.view:hover {
  background-color: #1976d2;
  box-shadow: 0 3px 8px rgb(33 150 243 / 0.5);
}

.action-btn.edit {
  background-color: #ff9800;
  color: white;
}

.action-btn.edit:hover {
  background-color: #f57c00;
  box-shadow: 0 3px 8px rgb(255 152 0 / 0.5);
}

.action-btn.delete {
  background-color: #f44336;
  color: white;
}

.action-btn.delete:hover {
  background-color: #d32f2f;
  box-shadow: 0 3px 8px rgb(244 67 54 / 0.5);
}

.dialog-overlay {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.25s ease forwards;
}

.dialog-content {
  background-color: #fafafa;
  padding: 2rem 2.5rem;
  border-radius: 12px;
  width: 480px;
  max-width: 90%;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.18);
  animation: slideDown 0.3s ease forwards;
}

.dialog-content h3 {
  margin-top: 0;
  margin-bottom: 1.25rem;
  color: #2c3e50;
  border-bottom: 2px solid #e0e6ed;
  padding-bottom: 0.75rem;
  font-size: 1.4rem;
  font-weight: 700;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  color: #555;
  font-weight: 600;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 0.55rem 0.8rem;
  border: 1.5px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.25s ease, box-shadow 0.25s ease;
  background-color: #fff;
  color: #333;
}

.form-group input::placeholder {
  color: #b6b6b6;
  font-style: italic;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #4caf50;
  box-shadow: 0 0 8px rgba(76, 175, 80, 0.4);
  background-color: #fff;
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 1.75rem;
  gap: 1rem;
}

.save-btn {
  background: linear-gradient(135deg, #4caf50, #388e3c);
  padding: 0.6rem 1.5rem;
  color: white;
  border-radius: 8px;
  border: none;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.3s ease, box-shadow 0.25s ease;
  white-space: nowrap;
}

.save-btn:hover {
  background: linear-gradient(135deg, #45a049, #2e7d32);
  box-shadow: 0 4px 14px rgba(67, 160, 71, 0.6);
}

.cancel-btn {
  padding: 0.6rem 1.5rem;
  background-color: #f44336;
  color: white;
  border-radius: 8px;
  border: none;
  font-weight: 700;
  cursor: pointer;
  transition: background-color 0.3s ease, box-shadow 0.25s ease;
  white-space: nowrap;
}

.cancel-btn:hover {
  background-color: #d32f2f;
  box-shadow: 0 4px 14px rgba(211, 47, 47, 0.6);
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-15px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
