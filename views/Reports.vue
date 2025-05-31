<script setup>
import { ref } from "vue";
import axios from "axios";

const loading = ref(false);
const report = ref(null);
const errorMsg = ref("");

const generateReport = async () => {
  loading.value = true;
  errorMsg.value = "";
  report.value = null;
  try {
    const res = await axios.get("/api/reports/generate");
    report.value = res.data;
  } catch (error) {
    console.error("报告生成失败", error);
    errorMsg.value = "报告生成失败，请稍后重试。";
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="container">
    <h2 class="title">生成考勤报告</h2>
    <button class="btn" @click="generateReport" :disabled="loading">
      {{ loading ? "生成中..." : "生成报告" }}
    </button>

    <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>

    <div v-if="report" class="report-container">
      <section class="section">
        <h3>班级信息</h3>
        <p>
          班级名称：<strong>{{ report.className }}</strong>
        </p>
        <p>
          辅导员：<strong>{{ report.counselor }}</strong>
        </p>
        <p>
          班级人数：<strong>{{ report.totalStudents }}</strong>
        </p>
      </section>

      <section class="section">
        <h3>考勤汇总</h3>
        <table class="report-table">
          <thead>
            <tr>
              <th>学生姓名</th>
              <th>学号</th>
              <th>出勤天数</th>
              <th>迟到</th>
              <th>早退</th>
              <th>缺勤</th>
              <th>请假</th>
              <th>备注</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="student in report.attendanceList"
              :key="student.studentId"
            >
              <td>{{ student.name }}</td>
              <td>{{ student.studentId }}</td>
              <td>{{ student.presentDays }}</td>
              <td>{{ student.lateCount }}</td>
              <td>{{ student.leftEarlyCount }}</td>
              <td>{{ student.absentCount }}</td>
              <td>{{ student.leaveCount }}</td>
              <td>{{ student.remark || "-" }}</td>
            </tr>
          </tbody>
        </table>
      </section>

      <section class="section stats">
        <h3>整体统计</h3>
        <p>
          <span>出勤率：</span><strong>{{ report.attendanceRate }}%</strong>
        </p>
        <p>
          <span>缺勤率：</span><strong>{{ report.absenceRate }}%</strong>
        </p>
        <p>
          <span>迟到率：</span><strong>{{ report.lateRate }}%</strong>
        </p>
      </section>

      <section class="section abnormal">
        <h3>异常提醒</h3>
        <ul>
          <li v-if="report.abnormalStudents.length === 0">暂无异常记录</li>
          <li
            v-for="student in report.abnormalStudents"
            :key="student.studentId"
          >
            {{ student.name }} (缺勤 {{ student.absentCount }} 天，迟到
            {{ student.lateCount }} 次)
          </li>
        </ul>
      </section>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 900px;
  margin: 30px auto;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  color: #2c3e50;
}

.btn {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 10px 25px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  display: block;
  margin: 0 auto 20px;
  transition: background-color 0.3s ease;
}

.btn:disabled {
  background-color: #a5d6a7;
  cursor: not-allowed;
}

.btn:hover:not(:disabled) {
  background-color: #339966;
}

.error-msg {
  color: #e74c3c;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}

.report-container {
  border-top: 2px solid #42b983;
  padding-top: 20px;
}

.section {
  margin-bottom: 25px;
}

.report-table {
  width: 100%;
  border-collapse: collapse;
}

.report-table th,
.report-table td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: center;
}

.report-table th {
  background-color: #42b983;
  color: white;
}

.stats p {
  font-size: 1.1rem;
  margin: 8px 0;
}

.stats span {
  color: #555;
}

.abnormal ul {
  list-style-type: disc;
  padding-left: 20px;
  color: #c0392b;
  font-weight: 600;
}
</style>
