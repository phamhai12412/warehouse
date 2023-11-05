import { ACCEPT_PRODUCT, ADD_PRODUCT, DELETE_PRODUCT, GET_ALL_PRODUCT, GET_ALL_PRODUCT_BY_STORAGE_ID, UNDELETE_PRODUCT, UPDATE_PRODUCT } from "../api/service/productService";

import { createSlice } from "@reduxjs/toolkit";

const productSlice = createSlice({
  name: 'product',
  initialState: [],
  reducers: {

  },
  extraReducers: (builder) => {
    builder
      .addCase(GET_ALL_PRODUCT.fulfilled, (state, action) => {
        return action.payload.content;
      })
      .addCase(ADD_PRODUCT.fulfilled, (state, action) => {
        state.push(action.payload);
        return state;
      })
      .addCase(GET_ALL_PRODUCT_BY_STORAGE_ID.fulfilled, (state, action) => {
        return [...action.payload.content];
      })
      .addCase(UPDATE_PRODUCT.fulfilled, (state, action) => {
        return state.map((item) => {
          if (item.id === action.payload.id) {
            item = action.payload;
          }
        })
      })
      .addCase(ACCEPT_PRODUCT.fulfilled, (state, action) => {
        return state.map((item) => {
          if (item.id === action.payload.id) {
            item = action.payload;
          }
        })
      })
      .addCase(DELETE_PRODUCT.fulfilled, (state, action) => {
        return state.map((item) => {
          if (item.id === action.payload.id) {
            item = action.payload;
          }
        })
      })
      .addCase(UNDELETE_PRODUCT.fulfilled, (state, action) => {
        return state.map((item) => {
          if (item.id === action.payload.id) {
            item = action.payload;
          }
        })
      })
  }
})

export const { } = productSlice.actions;
export default productSlice.reducer;