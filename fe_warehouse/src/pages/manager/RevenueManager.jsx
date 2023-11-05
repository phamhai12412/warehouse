import React, { useEffect, useState } from 'react'

import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import ChartRevenue from '../../components/dashboard/ChartRevenue';
import { Cookies } from 'react-cookie';
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import instance from '../../redux/api';
import { useNavigate } from 'react-router-dom';

function RevenueManager() {

  const navigate = useNavigate();

  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

  const [data, setData] = useState(null);

  const [yearValue, setYearValue] = useState((new Date()).getFullYear());

  const handleChangeYearValue = (e) => {
    setYearValue(e.$y);
  }

  const handleLoadRevenue = () => {
    instance.get(`/api/v1/dashboard/GVCStorageId/${user.storageId}/?yearValue=${yearValue}`, {
      headers: {
        Authorization: `Bearer ${new Cookies().get('token')}`
      }
    })
      .then((resp) => setData(resp.data.map((item) => ({ name: item.name, "Doanh Thu": item.gvc }))))
      .catch((err) => console.log(err));
  }

  useEffect(() => {
    if (user) {
      handleLoadRevenue();
    } else {
      navigate('/')
    }

  }, [yearValue])

  return (
    <div>
      <div className='flex justify-center uppercase text-2xl font-semibold py-5'>quản lý doanh thu đơn hàng</div>
      <div className='px-2'>
        <div className='flex justify-center '>
          <div className='bg-white p-3 rounded-md shadow-xl'>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DemoContainer components={["DatePicker"]}>
                <DatePicker label={yearValue} views={["year"]} onChange={handleChangeYearValue} />
              </DemoContainer>
            </LocalizationProvider>
          </div>
        </div>
      </div>
      <div className='flex flex-col gap-5 px-10 py-10'>
        <ChartRevenue data={data} />
      </div>
    </div>
  )
}

export default RevenueManager