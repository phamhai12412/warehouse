import { Cookies } from "react-cookie";
import { createAsyncThunk } from "@reduxjs/toolkit";
import instance from "../index.jsx"

export const GET_ALL_USER = createAsyncThunk("user/GETALL", async (search) => {
  if (search) {
    return await instance.get(`/api/v1/users/?name=${search}`, {
      headers: {
        Authorization: `Bearer ${new Cookies().get("token")}`
      }
    }).then((resp) => resp.data);
  }
  return await instance.get("/api/v1/users", {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then((resp) => resp.data);
})


export const REGISTER_USER = createAsyncThunk("user/REGISTER", async (registerForm) => {
  return await instance.post("/api/v1/sign/sign-up", registerForm, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  })
    .then((resp) => resp.data);
})

export const CHANGE_STATUS_USER = createAsyncThunk(
  "user/CHANGESTATUSUSER", async (id) => {
    return await instance.put(`/api/v1/users/status/${id}`, {}, {
      headers: {
        Authorization: `Bearer ${new Cookies().get("token")}`
      }
    })
      .then((resp) => resp.data)
  }
)

export const UPDATE_USER = createAsyncThunk(
  "user/UPDATEUSER", async ({ data, id }) => {
    return await instance.put(`/api/v1/users/update_user/${id}`, data, {
      headers: {
        Authorization: `Bearer ${new Cookies().get("token")}`
      }
    }).then(resp => resp.data);
  }
)