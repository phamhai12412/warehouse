import { createSlice } from "@reduxjs/toolkit";

const authSlice = createSlice({
  name: "auth",
  initialState: null,
  reducers: {
    setUser: (state, action) => {
      return action.payload;
    },
    resetUser: (state, action) => {
      return null;
    },
  },
});

export const { setUser, resetUser } = authSlice.actions;
export default authSlice.reducer;
