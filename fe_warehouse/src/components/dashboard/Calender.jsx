import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DateCalendar } from "@mui/x-date-pickers/DateCalendar";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import React from "react";

function Calender() {
  return (
    <div style={{ background: "#fff" }}>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <DateCalendar sx={{ width: "100%" }} />
      </LocalizationProvider>
    </div>
  );
}

export default Calender;
