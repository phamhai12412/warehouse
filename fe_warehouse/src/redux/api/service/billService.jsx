import { Cookies } from "react-cookie";
import { createAsyncThunk } from "@reduxjs/toolkit";
import instance from "..";

export const GET_ALL_BILL = createAsyncThunk("billRemote/GET_ALL_BILL", async ({ filter, search }) => {
  return await instance.get(`/api/v1/shipping-report/all-by-status/?deliveryName=${filter}&search=${search}`, {
    headers: `Bearer ${new Cookies().get("token")}`
  }).then((resp) => resp.data);

})

export const ADD_BILL = createAsyncThunk("billRemote/ADD_BILL", async (data) => {
  return await instance.post("/api/v1/shipping-report/create", data, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then((resp) => resp.data);
})

export const REJECT_BILL = createAsyncThunk("billRemote/REJECT_BILL", async (id) => {
  return await instance.put(`/api/v1/shipping-report/rejectBill/${id}`, {}, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then((resp) => resp.data);
})

export const ACCEPT_BILL = createAsyncThunk("billRemote/ACCEPT_BILL", async (id) => {
  return await instance.put(`/api/v1/shipping-report/approveBill/${id}`, {}, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then((resp) => resp.data);
})

export const DELIVERY_BILL = createAsyncThunk("billRemote/DELIVERY_BILL", async (id) => {
  return await instance.put(`/api/v1/shipping-report/delivery/${id}`, {}, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then((resp) => resp.data);
})

export const SUCCESS_BILL = createAsyncThunk("billRemote/SUCCESS_BILL", async (id) => {
  return await instance.put(`/api/v1/shipping-report/success/${id}`, {}, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then((resp) => resp.data);
})

export const UPDATE_BILL = createAsyncThunk("billRemote/UPDATE_BILL", async ({ data, id }) => {
  return await instance.put(`/api/v1/shipping-report/updateAll/${id}`, data, {
    headers: {
      Authorization: `Bearer ${new Cookies().get('token')}`
    }
  }).then(resp => resp.data);
})