import { Cookies } from "react-cookie";
import { createAsyncThunk } from "@reduxjs/toolkit";
import instance from "..";

export const GET_ALL_SHIP = createAsyncThunk("ship/GET_ALL_SHIP", async (search) => {
  return await instance.get(`/api/v1/shipment/?name=${search}`, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data);
})

export const ADD_SHIP = createAsyncThunk("ship/ADD_SHIP", async (data) => {
  return await instance.post("/api/v1/shipment", data, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data)
})

export const UPDATE_SHIP = createAsyncThunk("ship/UPDATE_SHIP", async ({ data, id }) => {
  return await instance.put(`/api/v1/shipment/${id}`, data, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data);
})

export const CHANGE_STATUS_SHIP = createAsyncThunk("ship/CHANGE_STATUS_SHIP", async (id) => {
  return await instance.put(`/api/v1/shipment/status/${id}`, {}, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data)
})