import { CHANGE_STATUS_USER, GET_ALL_USER, REGISTER_USER, UPDATE_USER } from "../api/service/userService";

import { createSlice } from "@reduxjs/toolkit";

const userSlice = createSlice({
  name: "user",
  initialState: [],
  reducers: {
    changeStatusUser: (state, action) => {
      return state.map((item) => {
        if (item.id === action.payload) {
          item = action.payload;
        }
      })
    },
    addNewUser: (state, action) => {
      return [...state, action.payload];
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(GET_ALL_USER.fulfilled, (state, action) => {
        return action.payload.content;
      })
      .addCase(REGISTER_USER.fulfilled, (state, action) => {
        state.push(action.payload);
        return state;
      })
      .addCase(UPDATE_USER.fulfilled, (state, action) => {
        return state.map((item) => {
          if (item.id === action.payload.id) {
            item = action.payload;
          }
        })
      })
  }
})

export const { changeStatusUser, addNewUser } = userSlice.actions;
export default userSlice.reducer;