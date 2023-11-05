import { ArcElement, Chart as ChartJS, Legend, Tooltip } from 'chart.js';
import React, { useEffect, useState } from 'react'

import { Doughnut } from 'react-chartjs-2';
import instance from '../../redux/api';

ChartJS.register(ArcElement, Tooltip, Legend);

function DoughtnutChart() {

  const [data, setData] = useState(null);

  const handleLoadData = () => {
    instance.get('/api/v1/dashboard/transformed-data')
      .then(resp => setData(resp.data))
      .catch(err => console.log(err));
  }

  useEffect(() => {
    handleLoadData();
  }, [])

  return (
    <div className='px-16'>
      {data && <Doughnut data={data} />}
    </div>
  )
}

export default DoughtnutChart