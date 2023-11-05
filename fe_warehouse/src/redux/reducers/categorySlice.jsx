import { ADD_CATEGORY, GET_ALL_CATEGORY, UPDATE_CATEGORY } from "../api/service/categoryService";

import { createSlice } from "@reduxjs/toolkit";

const categorySlice = createSlice({
  name: "category",
  initialState: [],
  reducers: {
    changeStatusCategory: (state, action) => {
      return state.map(item => {
        if (item.id === action.payload) {
          item = action.payload;
        }
      })
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(GET_ALL_CATEGORY.fulfilled, (state, action) => {
        return action.payload.content;
      })
      .addCase(UPDATE_CATEGORY.fulfilled, (state, action) => {
        return state.map(item => {
          if (item.id === action.payload.id) {
            item = action.payload;
          }
        })
      })
      .addCase(ADD_CATEGORY.fulfilled, (state, action) => {
        return [...state, action.payload];
      })
  }
})

export const { changeStatusCategory } = categorySlice.actions;
export default categorySlice.reducer;