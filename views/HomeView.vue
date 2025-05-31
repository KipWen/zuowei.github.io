<script setup>
import { ref } from "vue";

const welcomeMessage = ref("学生考勤管理系统");
const studentCount = ref(120);
const attendanceStats = ref({
  present: 100,
  absent: 20,
  late: 5,
});

// 计算出勤率
const attendanceRate = ref(
  Math.round((attendanceStats.value.present / studentCount.value) * 100)
);
</script>

<template>
  <div class="home-container">
    <div class="header">
      <h1>{{ welcomeMessage }}</h1>
      <p class="subtitle">高效管理学生出勤情况，一键掌握班级动态</p>
    </div>

    <div class="stats">
      <div class="stat-card">
        <div class="stat-icon">👨🎓</div>
        <h3>学生总数</h3>
        <p class="stat-value">{{ studentCount }}</p>
        <p class="stat-label">人</p>
      </div>

      <div class="stat-card">
        <div class="stat-icon">✅</div>
        <h3>出勤人数</h3>
        <p class="stat-value">{{ attendanceStats.present }}</p>
        <p class="stat-label">人</p>
      </div>

      <div class="stat-card highlight">
        <div class="stat-icon">📊</div>
        <h3>出勤率</h3>
        <p class="stat-value">{{ attendanceRate }}%</p>
        <div class="progress-bar">
          <div class="progress" :style="{ width: attendanceRate + '%' }"></div>
        </div>
      </div>
    </div>

    <div class="details-grid">
      <div class="detail-card">
        <h3>缺勤情况</h3>
        <div class="detail-item">
          <span class="badge absent">缺勤</span>
          <span class="count">{{ attendanceStats.absent }}人</span>
        </div>
        <div class="detail-item">
          <span class="badge late">迟到</span>
          <span class="count">{{ attendanceStats.late }}人</span>
        </div>
      </div>

      <div class="detail-card quick-actions">
        <h3>快捷操作</h3>
        <ul>
          <li>
            <router-link to="/attendance" class="action-link">
              <span class="icon">📝</span>
              <span>考勤记录</span>
            </router-link>
          </li>
          <li>
            <router-link to="/students" class="action-link">
              <span class="icon">👥</span>
              <span>学生信息</span>
            </router-link>
          </li>
          <li>
            <router-link to="/reports" class="action-link">
              <span class="icon">📊</span>
              <span>考勤报告</span>
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

.header {
  text-align: center;
  margin-bottom: 3rem;
}

.header h1 {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 0.5rem;
  font-weight: 600;
  background: linear-gradient(135deg, #3498db, #2ecc71);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.subtitle {
  font-size: 1.1rem;
  color: #7f8c8d;
  margin-top: 0;
}

.stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.stat-card.highlight {
  background: linear-gradient(135deg, #f5f7fa, #e4e8eb);
  border: 1px solid #e0e6ed;
}

.stat-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.stat-value {
  font-size: 2.2rem;
  font-weight: 700;
  margin: 0.5rem 0;
  color: #2c3e50;
}

.stat-label {
  color: #7f8c8d;
  font-size: 0.9rem;
  margin: 0;
}

.progress-bar {
  height: 8px;
  background: #ecf0f1;
  border-radius: 4px;
  margin-top: 1rem;
  overflow: hidden;
}

.progress {
  height: 100%;
  background: linear-gradient(90deg, #2ecc71, #3498db);
  border-radius: 4px;
  transition: width 0.5s ease;
}

.details-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

@media (max-width: 768px) {
  .details-grid {
    grid-template-columns: 1fr;
  }
}

.detail-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.detail-card h3 {
  margin-top: 0;
  margin-bottom: 1.5rem;
  color: #2c3e50;
  font-size: 1.2rem;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.5rem;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.8rem 0;
  border-bottom: 1px solid #f5f5f5;
}

.detail-item:last-child {
  border-bottom: none;
}

.badge {
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
}

.badge.absent {
  background: #ffebee;
  color: #e53935;
}

.badge.late {
  background: #fff8e1;
  color: #ff8f00;
}

.count {
  font-weight: 600;
  color: #2c3e50;
}

.quick-actions ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.quick-actions li {
  margin-bottom: 0.8rem;
}

.quick-actions li:last-child {
  margin-bottom: 0;
}

.action-link {
  display: flex;
  align-items: center;
  padding: 0.8rem 1rem;
  text-decoration: none;
  color: #2c3e50;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.action-link:hover {
  background: #f5f7fa;
  transform: translateX(5px);
}

.action-link .icon {
  font-size: 1.2rem;
  margin-right: 1rem;
  width: 30px;
  text-align: center;
}
</style>
