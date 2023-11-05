import { ACCEPT_BILL, ADD_BILL, DELIVERY_BILL, GET_ALL_BILL, REJECT_BILL, SUCCESS_BILL, UPDATE_BILL } from "../api/service/billService";

import { createSlice } from "@reduxjs/toolkit";

const billRemoteSlice = createSlice({
  name: 'billRemote',
  initialState: [],
  reducers: {

  },
  extraReducers: (builder) => {
    builder
      .addCase(GET_ALL_BILL.fulfilled, (state, action) => {
        return [...action.payload.content];
      })
      .addCase(ADD_BILL.fulfilled, (state, action) => {
        return [...state, action.payload];
      })
      .addCase(UPDATE_BILL.fulfilled, (state, action) => {
        return state.map((item) => {
          if (item.id === action.payload.id) {
            item = action.payload;
          }
        })
      })
      .addCase(REJECT_BILL.fulfilled, (state, action) => {
        return state.map((item) => {
          if (item.id === action.payload.id) {
            item = action.payload;
          }
        })
      })
      .addCase(ACCEPT_BILL.fulfilled, (state, action) => {
        return state.map((item) => {
          if (item.id === action.payload.id) {
            item = action.payload;
          }
        })
      })
      .addCase(DELIVERY_BILL.fulfilled, (state, action) => {
        return state.map((item) => {
          if (item.id === action.payload.id) {
            item = action.payload;
          }
        })
      })
      .addCase(SUCCESS_BILL.fulfilled, (state, action) => {
        return state.map((item) => {
          if (item.id === action.payload.id) {
            item = action.payload;
          }
        })
      })
  }
})

export const { } = billRemoteSlice.actions;
export default billRemoteSlice.reducer;