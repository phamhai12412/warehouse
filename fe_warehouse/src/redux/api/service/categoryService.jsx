import { Cookies } from "react-cookie";
import { createAsyncThunk } from "@reduxjs/toolkit";
import instance from "..";

export const GET_ALL_CATEGORY = createAsyncThunk("category/GETALLCATEGORY", async (search) => {
  return await instance.get(`/api/v1/categories/?name=${search}`, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data);
})

export const ADD_CATEGORY = createAsyncThunk("category/ADDCATEGORY", async (data) => {
  return await instance.post("/api/v1/categories", data, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data)
})

export const UPDATE_CATEGORY = createAsyncThunk("category/UPDATECATEGORY", async ({ data, id }) => {
  return await instance.put(`/api/v1/categories/${id}`, data, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data)
})

export const CHANGE_STATUS_CATEGORY = createAsyncThunk("category/CHANGESTATUSCATEGORY", async (id) => {
  return await instance.put(`/api/v1/categories/changeStatus/${id}`, {}, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  })
})