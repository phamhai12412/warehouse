import { CircularProgress } from '@mui/material'
import React from 'react'

function LoadingComponent() {
  return (
    <div className='bg-white border-black shadow-lg flex justify-center items-center h-32 w-full'>
      <CircularProgress />
    </div>
  )
}

export default LoadingComponent