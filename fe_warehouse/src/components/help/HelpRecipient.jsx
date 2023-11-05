import ArrowRightIcon from '@mui/icons-material/ArrowRight';
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord';
import React from 'react'

function HelpRecipient() {
  return (
    <div>
      <p>
        {/* <p><b><FiberManualRecordIcon /> Nơi Nhận Hàng : </b></p> */}
        <p><ArrowRightIcon /> WH <i className="fa-solid fa-truck-moving"></i> WH | SMK </p>
        <p><ArrowRightIcon /> SMK <i className="fa-solid fa-truck-moving"></i> SMK</p>
        <p><ArrowRightIcon /> VD: SMK-HN1229921 </p>
        <p><ArrowRightIcon /> SMK : supermarket : siêu thị</p>
        <p><ArrowRightIcon /> WH : warehouse : nhà kho</p>
        <p><ArrowRightIcon /> HN : Hà Nội </p>
        <p><ArrowRightIcon /> DN : Đà Nẵng </p>
        <p><ArrowRightIcon /> HCM : Hồ Chí Minh </p>
        <p><ArrowRightIcon /> xxx.xxx Là mã của kho hoặc siêu thị</p>
      </p>
    </div>
  )
}

export default HelpRecipient