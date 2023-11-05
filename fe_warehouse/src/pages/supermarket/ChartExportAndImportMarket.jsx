import React, { useEffect, useState } from 'react'

import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import BarChartExport from '../../components/dashboard/BarChartExport'
import BarChartImport from '../../components/dashboard/BarChartImport'
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import instance from '../../redux/api';
import { useNavigate } from 'react-router-dom';

function ChartExportAndImportMarket() {

  const navigate = useNavigate();

  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

  const [dataExport, setDataExport] = useState(null);
  const [dataImport, setDataImport] = useState(null);

  const [yearValue, setYearValue] = useState((new Date()).getFullYear());

  const handleChangeYearValue = (e) => {
    setYearValue(e.$y);
  }

  const handleLoadDataExport = () => {
    instance.get(`/api/v1/dashboard/dashboard-export/${user.storageId}/?yearValue=${yearValue}`)
      .then(resp => setDataExport(resp.data.map(item => ({ name: item.name, "đơn thành công": item.sc, "đơn hủy": item.cc }))))
      .catch(err => console.log(err))
  }
  const handleLoadDataImport = () => {
    instance.get(`/api/v1/dashboard/dashboard-import/${user.storageId}/?yearValue=${yearValue}`)
      .then(resp => setDataImport(resp.data.map(item => ({ name: item.name, "đơn thành công": item.sc, "đơn hủy": item.cc }))))
      .catch(err => console.log(err))
  }

  useEffect(() => {
    if (user) {
      handleLoadDataExport();
      handleLoadDataImport();
    } else {
      navigate('/');
    }

  }, [yearValue])

  return (
    <div>
      <div className='flex justify-center uppercase text-2xl font-semibold py-5'>quản lý đơn hàng nhập xuất</div>
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
      <div className='flex flex-col gap-5 px-10 py-5'>
        <BarChartExport dataExport={dataExport} />
        <BarChartImport dataImport={dataImport} />
      </div>
    </div>
  )
}

export default ChartExportAndImportMarket