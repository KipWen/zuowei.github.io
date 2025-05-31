<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

// 状态变量
const attendanceRecords = ref([]);
const editingRecordId = ref(null);
const editStatus = ref("");
const error = ref(null);
const showAddForm = ref(false); // 控制表单显示
const newRecord = ref({
  // 新记录表单数据
  studentName: "",
  date: "",
  status: "出勤",
});

// 获取考勤记录
const fetchAttendanceRecords = async () => {
  try {
    const response = await axios.get("/api/attendance");
    attendanceRecords.value = response.data.map((record) => ({
      ...record,
      date: formatDate(record.date),
    }));
  } catch (err) {
    error.value = err.message;
    console.error("获取考勤记录失败", err);
  }
};

// 添加记录
const addRecord = async () => {
  try {
    const recordToSend = {
      ...newRecord.value,
      date: new Date(newRecord.value.date).toISOString().split("T")[0],
    };

    const response = await axios.post("/attendance", recordToSend);
    if (response.status === 200) {
      await fetchAttendanceRecords();
      showAddForm.value = false;
      newRecord.value = { studentName: "", date: "", status: "出勤" };
    }
  } catch (error) {
    console.error("添加失败:", error.response?.data || error.message);
  }
};

const formatDate = (dateStr) => {
  const date = new Date(dateStr);
  return `${date.getFullYear()}-${(date.getMonth() + 1)
    .toString()
    .padStart(2, "0")}-${date.getDate().toString().padStart(2, "0")}`;
};

const startEdit = (record) => {
  editingRecordId.value = record.id;
  editStatus.value = record.status;
};

const cancelEdit = () => {
  editingRecordId.value = null;
  editStatus.value = "";
};

const saveEdit = async (recordId) => {
  try {
    await axios.put(`/api/attendance/${recordId}`, {
      status: editStatus.value,
    });
    await fetchAttendanceRecords();
    editingRecordId.value = null;
  } catch (err) {
    error.value = err.message;
    console.error("更新考勤记录失败", err);
  }
};

onMounted(() => {
  fetchAttendanceRecords();
});
</script>

<template>
  <div class="attendance-container">
    <h2 class="page-title">学生考勤记录</h2>

    <!-- 新增的“添加”按钮 -->
    <div class="btn-add-wrapper">
      <button class="btn edit-btn" @click="showAddForm = true">添加</button>
    </div>

    <div v-if="showAddForm" class="add-form">
      <h3>添加考勤记录</h3>
      <div class="form-group">
        <label>学生姓名:</label>
        <input
          v-model="newRecord.studentName"
          type="text"
          placeholder="请输入学生姓名"
        />
      </div>
      <div class="form-group">
        <label>日期:</label>
        <input v-model="newRecord.date" type="date" />
      </div>
      <div class="form-group">
        <label>状态:</label>
        <select v-model="newRecord.status">
          <option value="出勤">出勤</option>
          <option value="迟到">迟到</option>
          <option value="早退">早退</option>
          <option value="缺勤">缺勤</option>
          <option value="请假">请假</option>
        </select>
      </div>
      <div class="form-actions">
        <button class="btn save-btn" @click="addRecord">保存</button>
        <button class="btn cancel-btn" @click="showAddForm = false">
          取消
        </button>
      </div>
    </div>

    <div class="table-wrapper">
      <table class="attendance-table">
        <thead>
          <tr>
            <th>学生姓名</th>
            <th>日期</th>
            <th>出勤状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in attendanceRecords" :key="record.id">
            <td>{{ record.studentName }}</td>
            <td>{{ record.date }}</td>
            <td>
              <template v-if="editingRecordId === record.id">
                <select v-model="editStatus" class="status-select">
                  <option value="出勤">出勤</option>
                  <option value="迟到">迟到</option>
                  <option value="早退">早退</option>
                  <option value="缺勤">缺勤</option>
                  <option value="请假">请假</option>
                </select>
              </template>
              <template v-else>
                <span :class="'status-' + record.status">{{
                  record.status
                }}</span>
              </template>
            </td>
            <td>
              <template v-if="editingRecordId === record.id">
                <button @click="saveEdit(record.id)" class="btn save-btn">
                  保存
                </button>
                <button @click="cancelEdit" class="btn cancel-btn">取消</button>
              </template>
              <template v-else>
                <button @click="startEdit(record)" class="btn edit-btn">
                  修改
                </button>
              </template>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.attendance-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
  font-family: "Arial", sans-serif;
}

.page-title {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 15px; /* 从25改小一点，为按钮腾位置 */
  font-size: 24px;
}

/* 新增“添加”按钮容器，居中显示，和按钮底部间距 */
.btn-add-wrapper {
  text-align: center;
  margin-bottom: 20px;
}

/* 按钮复用已有样式 btn 和 edit-btn */

.table-wrapper {
  overflow-x: auto;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.attendance-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

.attendance-table th,
.attendance-table td {
  padding: 12px 15px;
  text-align: center;
  border-bottom: 1px solid #e0e0e0;
}

.attendance-table th {
  background-color: #f8f9fa;
  color: #495057;
  font-weight: 600;
}

.attendance-table tr:hover {
  background-color: #f5f5f5;
}

.btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  margin: 0 3px;
  transition: all 0.3s;
}

.edit-btn {
  background-color: #3498db;
  color: white;
}

.edit-btn:hover {
  background-color: #2980b9;
}

.save-btn {
  background-color: #2ecc71;
  color: white;
}

.save-btn:hover {
  background-color: #27ae60;
}

.cancel-btn {
  background-color: #e74c3c;
  color: white;
}

.cancel-btn:hover {
  background-color: #c0392b;
}

.status-select {
  padding: 5px;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.status-出勤 {
  color: #2ecc71;
  font-weight: bold;
}

.status-迟到,
.status-早退 {
  color: #f39c12;
  font-weight: bold;
}

.status-缺勤 {
  color: #e74c3c;
  font-weight: bold;
}

.status-请假 {
  color: #3498db;
  font-weight: bold;
}

/* 新增表单样式 */
.add-form {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.add-form h3 {
  margin-top: 0;
  color: #2c3e50;
  text-align: center;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}
</style>
