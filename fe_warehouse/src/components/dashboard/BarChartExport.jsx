import { Bar, BarChart, CartesianGrid, Legend, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";

import React from 'react'

function BarChartExport({ dataExport }) {

  return (
    <div className="bg-white pb-20" style={{ height: '650px' }}>
      <div>
        <h1 className="text-center p-3 text-2xl uppercase">Hoạt động xuất hàng</h1>
      </div>
      <ResponsiveContainer width="100%" height="100%">
        <BarChart
          data={dataExport}
          margin={{
            top: 5,
            right: 30,
            left: 20,
            bottom: 5
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="đơn thành công" fill="#82ca9d" />
          <Bar dataKey="đơn hủy" fill="#e74c3c" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}

export default BarChartExport