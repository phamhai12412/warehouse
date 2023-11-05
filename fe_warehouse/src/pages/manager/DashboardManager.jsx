import React, { useEffect, useState } from 'react'

import BarChartExport from '../../components/dashboard/BarChartExport'
import BillSuccess from '../../components/dashboard/BillSuccess'
import Calender from '../../components/dashboard/Calender'
import ResultManager from '../../components/dashboard/ResultManager'
import instance from '../../redux/api'
import { useNavigate } from 'react-router-dom'

function DashboardManager() {

  const navigate = useNavigate();

  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

  const [dataExport, setDataExport] = useState(null);

  const handleLoadDataExport = () => {
    instance.get(`/api/v1/dashboard/dashboard-export/${user.storageId}/?yearValue=`)
      .then(resp => setDataExport(resp.data.map(item => ({ name: item.name, "đơn thành công": item.sc, "đơn hủy": item.cc }))))
      .catch(err => console.log(err))
  }

  useEffect(() => {
    if (user) {
      handleLoadDataExport();
    } else {
      navigate('/');
    }
  }, [])

  return (
    <div className='flex'>
      <div className='p-7 flex flex-col gap-7' style={{ width: "75%" }}>
        <ResultManager />
        <BarChartExport dataExport={dataExport} />
        <BillSuccess />
      </div>
      <div className="p-7 flex-1">
        <Calender />
      </div>
    </div>
  )
}

export default DashboardManager