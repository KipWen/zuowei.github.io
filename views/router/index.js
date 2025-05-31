import { createRouter, createWebHistory } from "vue-router";
import LoginView from "../views/LoginView.vue";
import HomeView from "@/views/HomeView.vue";
import Attendance from "@/views/Attendance.vue";
import Students from "@/views/Students.vue";
import Reports from "@/views/Reports.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "login",
      component: LoginView,
    },
    {
      path: "/home",
      name: "home",
      component: HomeView,
    },

    {
      path: "/attendance",
      name: "Attendance",
      component: Attendance,
    },

    {
      path: "/Students",
      name: "Students",
      component: Students,
    },
    {
      path: "/Reports",
      name: "Reports",
      component: Reports,
    },
  ],
});

export default router;
