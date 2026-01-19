import React from 'react';
import { getImageUrl } from '../api';

const Avatar = ({ imagePath, size = 50, editable = false, onFileSelect, previewUrl }) => {

    const src = previewUrl || getImageUrl(imagePath);

    const style = {
        width: `${size}px`,
        height: `${size}px`,
        borderRadius: '50%',
    };

    return (
        <div
            className={`avatar-container ${editable ? 'editable' : ''}`}
            style={style}
            onClick={editable && onFileSelect ? onFileSelect : undefined}
        >
            {src ? (
                <img src={src} alt="Avatar" className="avatar-img" />
            ) : (
                // base profile img if there is no profile picture
                <img src="src/assets/idle-profile.png" alt="Avatar" className="avatar-img" />
            )}
        </div>
    );
};

export default Avatar;