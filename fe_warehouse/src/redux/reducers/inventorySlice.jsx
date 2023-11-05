import { ADD_INVENTORY, GET_ALL_INVENTORY_BY_STORAGE_ID } from "../api/service/inventoryService";

import { createSlice } from "@reduxjs/toolkit";

const inventorySlice = createSlice({
  name: "inventory",
  initialState: [],
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(GET_ALL_INVENTORY_BY_STORAGE_ID.fulfilled, (state, action) => {
        return action.payload.content;
      })
      .addCase(ADD_INVENTORY.fulfilled, (state, action) => {
        return [...state, action.payload];
      });
  }
})

export const { } = inventorySlice.actions;
export default inventorySlice.reducer;