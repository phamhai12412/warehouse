import { Cookies } from "react-cookie";
import { createAsyncThunk } from "@reduxjs/toolkit";
import instance from "../index";

export const GET_ALL_PRODUCT = createAsyncThunk("product/GETALLPRODUCT", async () => {
  return await instance.get("/api/v1/products", {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then((resp) => resp.data);
})

export const ADD_PRODUCT = createAsyncThunk("product/ADDPRODUCT", async (data) => {
  return await instance.post("/api/v1/products", data, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then((resp) => resp.data);
})

export const UPDATE_PRODUCT = createAsyncThunk("product/UPDATE_PRODUCT", async ({ data, id }) => {
  return await instance.put(`/api/v1/products/${id}`, data, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data).catch(err => console.log(err));
})

export const CHANGE_STATUS_PRODUCT = createAsyncThunk("product/CHANGESTATUSPRODUCT", async ({ id, statusName }) => {
  return await instance.put(`/api/v1/products/${id}/change-status/?statusName=${statusName}`, {}, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data);
})

export const GET_ALL_PRODUCT_BY_STORAGE_ID = createAsyncThunk("product/GET_ALL_PRODUCT_BY_STORAGE_ID", async ({ search, filter, id }) => {
  return await instance.get(`/api/v1/products/findAll/${id}/?category=${filter}&search=${search}`).then(resp => resp.data);
})

export const ACCEPT_PRODUCT = createAsyncThunk("product/ACCEPT_PRODUCT", async (productId) => {
  return await instance.put(`/api/v1/products/${productId}/approve`, {}, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data);
})

export const DELETE_PRODUCT = createAsyncThunk("product/DELETE_PRODUCT", async (productId) => {
  return await instance.put(`/api/v1/products/${productId}/reject`, {}, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data);
})

export const UNDELETE_PRODUCT = createAsyncThunk("product/UNDELETE_PRODUCT", async (productId) => {
  return await instance.put(`/api/v1/products/${productId}/accept`, {}, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data);
})