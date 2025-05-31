import axios from "axios";
 
const baseURL = '/api';
const instance = axios.create({baseURL})

export default instance
//传送json格式的post请求
export const postRequest = (url, params) => {
    return axios({
        method: 'post',
        url:  baseURL + url,
        data: params
    })
}